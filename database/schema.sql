--
-- PostgreSQL database dump
--

\restrict glRhVkfqcQORHn3cehq62Orc8rZfx9Z1GiUEUeC8ee9pPCdoxBua8LsKfO54rzQ

-- Dumped from database version 16.10 (Debian 16.10-1.pgdg13+1)
-- Dumped by pg_dump version 16.10 (Debian 16.10-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: favorites; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.favorites (
    id integer NOT NULL,
    media_id integer NOT NULL,
    user_id integer NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.favorites OWNER TO myuser;

--
-- Name: favorites_id_seq; Type: SEQUENCE; Schema: public; Owner: myuser
--

CREATE SEQUENCE public.favorites_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.favorites_id_seq OWNER TO myuser;

--
-- Name: favorites_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: myuser
--

ALTER SEQUENCE public.favorites_id_seq OWNED BY public.favorites.id;


--
-- Name: media_entries; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.media_entries (
    id integer NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    media_type character varying(20) NOT NULL,
    release_year integer,
    genre text[],
    age_restriction character varying(10),
    creator character varying(50) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT media_entries_media_type_check CHECK (((media_type)::text = ANY (ARRAY[('movie'::character varying)::text, ('series'::character varying)::text, ('game'::character varying)::text, ('book'::character varying)::text]))),
    CONSTRAINT media_entries_release_year_check CHECK (((release_year > 1800) AND ((release_year)::numeric <= (EXTRACT(year FROM CURRENT_DATE) + (5)::numeric)))),
    CONSTRAINT title_not_empty CHECK ((length(TRIM(BOTH FROM title)) > 0))
);


ALTER TABLE public.media_entries OWNER TO myuser;

--
-- Name: media_entries_id_seq; Type: SEQUENCE; Schema: public; Owner: myuser
--

CREATE SEQUENCE public.media_entries_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.media_entries_id_seq OWNER TO myuser;

--
-- Name: media_entries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: myuser
--

ALTER SEQUENCE public.media_entries_id_seq OWNED BY public.media_entries.id;


--
-- Name: rating_likes; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.rating_likes (
    id integer NOT NULL,
    rating_id integer NOT NULL,
    username character varying(50) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.rating_likes OWNER TO myuser;

--
-- Name: rating_likes_id_seq; Type: SEQUENCE; Schema: public; Owner: myuser
--

CREATE SEQUENCE public.rating_likes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rating_likes_id_seq OWNER TO myuser;

--
-- Name: rating_likes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: myuser
--

ALTER SEQUENCE public.rating_likes_id_seq OWNED BY public.rating_likes.id;


--
-- Name: ratings; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.ratings (
    id integer NOT NULL,
    media_id integer NOT NULL,
    user_id integer NOT NULL,
    stars integer NOT NULL,
    comment text,
    comment_approved boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ratings_stars_check CHECK (((stars >= 1) AND (stars <= 5)))
);


ALTER TABLE public.ratings OWNER TO myuser;

--
-- Name: ratings_id_seq; Type: SEQUENCE; Schema: public; Owner: myuser
--

CREATE SEQUENCE public.ratings_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ratings_id_seq OWNER TO myuser;

--
-- Name: ratings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: myuser
--

ALTER SEQUENCE public.ratings_id_seq OWNED BY public.ratings.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: myuser
--

CREATE TABLE public.users (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    password_hash character varying(255) NOT NULL,
    token character varying(255),
    favoritegenre character varying(255) DEFAULT 'Unknown'
);


ALTER TABLE public.users OWNER TO myuser;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: myuser
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: favorites id; Type: DEFAULT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.favorites ALTER COLUMN id SET DEFAULT nextval('public.favorites_id_seq'::regclass);


--
-- Name: media_entries id; Type: DEFAULT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.media_entries ALTER COLUMN id SET DEFAULT nextval('public.media_entries_id_seq'::regclass);


--
-- Name: rating_likes id; Type: DEFAULT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.rating_likes ALTER COLUMN id SET DEFAULT nextval('public.rating_likes_id_seq'::regclass);


--
-- Name: ratings id; Type: DEFAULT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.ratings ALTER COLUMN id SET DEFAULT nextval('public.ratings_id_seq'::regclass);


--
-- Data for Name: favorites; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.favorites (id, media_id, user_id, created_at) FROM stdin;
\.


--
-- Data for Name: media_entries; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.media_entries (id, title, description, media_type, release_year, genre, age_restriction, creator, created_at, updated_at) FROM stdin;
1	Harry Potter	book known by everyone	book	1900	{sci-fi}	12	testUser	2025-10-08 10:17:48.164442	2025-10-08 10:17:48.164442
2	Inception Updated2	Updated description2	movie	2010	{sci-fi,action}	16	testUser	2025-10-10 08:43:47.666077	2025-10-10 10:37:49.616
\.


--
-- Data for Name: rating_likes; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.rating_likes (id, rating_id, username, created_at) FROM stdin;
\.


--
-- Data for Name: ratings; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.ratings (id, media_id, user_id, stars, comment, comment_approved, created_at, updated_at) FROM stdin;
1	1	5	5	great	f	2025-10-09 11:04:16	2025-10-09 11:04:22
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: myuser
--

COPY public.users (id, username, password_hash, token) FROM stdin;
9	testUser2	$2b$12$eHXlnwh9582YQz/8kCvGXuOSCtJ/HCZFETDoZgV/lukdDQ58TKopG	testUser2-ae00adfd-7a8a-4d71-b2ca-c334e490d54f-mrpToken
10	testUser3	$2b$12$5OLOHW71horeihQnH5B6ReHEaw650PWNHfy3FYUlkellgV16ChSEu	testUser3-510a0569-6baf-4f3d-9f8b-6574c4e3f19c-mrpToken
5	testUser	$2b$12$GaOTel4oj9bfqrKm0jvrReUxNVvfhpvAf5D0L1R.yCd5Ntw7mVV6.	testUser-3d17adf0-668a-4167-b346-2dc723120d80-mrpToken
\.


--
-- Name: favorites_id_seq; Type: SEQUENCE SET; Schema: public; Owner: myuser
--

SELECT pg_catalog.setval('public.favorites_id_seq', 1, false);


--
-- Name: media_entries_id_seq; Type: SEQUENCE SET; Schema: public; Owner: myuser
--

SELECT pg_catalog.setval('public.media_entries_id_seq', 3, true);


--
-- Name: rating_likes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: myuser
--

SELECT pg_catalog.setval('public.rating_likes_id_seq', 1, false);


--
-- Name: ratings_id_seq; Type: SEQUENCE SET; Schema: public; Owner: myuser
--

SELECT pg_catalog.setval('public.ratings_id_seq', 1, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: myuser
--

SELECT pg_catalog.setval('public.users_id_seq', 10, true);


--
-- Name: favorites favorites_media_id_user_id_key; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_media_id_user_id_key UNIQUE (media_id, user_id);


--
-- Name: favorites favorites_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_pkey PRIMARY KEY (id);


--
-- Name: media_entries media_entries_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.media_entries
    ADD CONSTRAINT media_entries_pkey PRIMARY KEY (id);


--
-- Name: rating_likes rating_likes_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.rating_likes
    ADD CONSTRAINT rating_likes_pkey PRIMARY KEY (id);


--
-- Name: rating_likes rating_likes_rating_id_username_key; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.rating_likes
    ADD CONSTRAINT rating_likes_rating_id_username_key UNIQUE (rating_id, username);


--
-- Name: ratings ratings_media_id_username_key; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT ratings_media_id_username_key UNIQUE (media_id, user_id);


--
-- Name: ratings ratings_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT ratings_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: favorites favorites_media_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_media_id_fkey FOREIGN KEY (media_id) REFERENCES public.media_entries(id) ON DELETE CASCADE;


--
-- Name: favorites favorites_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: media_entries media_entries_creator_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.media_entries
    ADD CONSTRAINT media_entries_creator_fkey FOREIGN KEY (creator) REFERENCES public.users(username) ON DELETE CASCADE;


--
-- Name: rating_likes rating_likes_rating_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.rating_likes
    ADD CONSTRAINT rating_likes_rating_id_fkey FOREIGN KEY (rating_id) REFERENCES public.ratings(id) ON DELETE CASCADE;


--
-- Name: rating_likes rating_likes_username_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.rating_likes
    ADD CONSTRAINT rating_likes_username_fkey FOREIGN KEY (username) REFERENCES public.users(username) ON DELETE CASCADE;


--
-- Name: ratings ratings_media_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT ratings_media_id_fkey FOREIGN KEY (media_id) REFERENCES public.media_entries(id) ON DELETE CASCADE;


--
-- Name: ratings ratings_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT ratings_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict glRhVkfqcQORHn3cehq62Orc8rZfx9Z1GiUEUeC8ee9pPCdoxBua8LsKfO54rzQ

