--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;

--///////////////// PEOPLE TABLE /////////////////--
CREATE SEQUENCE people_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;

CREATE TABLE people ( 
	p_id integer NOT NULL PRIMARY KEY DEFAULT NEXTVAL('people_id_seq'::regclass),
	name varchar(255) NOT NULL,
	email varchar(255) NOT NULL
);

-- Insert a record for other tables that have references to user fields with no pid --
insert into people values(-1, 'null', 'null');

ALTER SEQUENCE people_id_seq OWNED BY people.p_id;

--///////////////// ITEMS TABLE /////////////////-- 
CREATE SEQUENCE items_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;

CREATE TABLE items (
	p_id integer NOT NULL references people(p_id) ON DELETE CASCADE,
	item_date timestamp with time zone not null,
	item_id integer NOT NULL PRIMARY KEY DEFAULT NEXTVAL('items_id_seq'::regclass),
	body text,
	title text,
	type varchar(255) NOT NULL
);


ALTER SEQUENCE items_id_seq OWNED BY items.item_id;

--///////////////// THREADS TABLE /////////////////-- 

CREATE TABLE threads (
	item_id integer NOT NULL references items(item_id) ON DELETE CASCADE,
	thread_id integer NOT NULL references items(item_id) ON DELETE CASCADE,
	PRIMARY KEY (item_id, thread_id) 
);

--///////////////// SILENTS TABLE /////////////////-- 

CREATE TABLE silents (
	item_id integer NOT NULL references items(item_id),
	p_id integer NOT NULL references people(p_id)
);

--///////////////// ISSUES TABLE /////////////////-- 

CREATE TABLE issues (
	item_id integer NOT NULL references items(item_id),
	status varchar(255),
	assignee_id integer references people(p_id),
	creation_ts timestamp with time zone,
	last_modified_ts timestamp with time zone,
	title varchar(512),
	description text,
	creator_id integer NOT NULL references people(p_id),
	keywords varchar(512),
	issue_num varchar(64)
);

--///////////////// ATTACHMENTS TABLE /////////////////-- 

CREATE TABLE attachments (
	item_id integer NOT NULL references items(item_id),
	title varchar(512),
	body text
);

--//////////////// DEPENDENCIES       /////////////////--

CREATE TABLE dependencies(
	item_id integer NOT NULL references items(item_id),
	depends_on_id integer NOT NULL references items(item_id)
);

--///////////////// LINKS TABLE /////////////////-- 

CREATE TABLE links (
	item_id integer NOT NULL references items(item_id) ON DELETE CASCADE,
	commit_id varchar(255) NOT NULL,
	confidence real NOT NULL,
	PRIMARY KEY(item_id, commit_id)
);
	
CREATE TABLE patterns ( 
	p_id1 varchar(255),
	p_id2 varchar(255),
	type varchar(255),
	passed integer,
	failed integer,
	PRIMARY KEY(p_id1, p_id2, type)
);

CREATE TABLE commit_patterns ( 
	commit_id varchar(255),
	p_id1 varchar(255),
	p_id2 varchar(255),
	type varchar(255),
	social_weight real,
	technical_weight real,
	technical_weight_fuzzy real,
	PRIMARY KEY(p_id1, p_id2, commit_id)
);