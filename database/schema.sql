--
-- PostgreSQL database dump
--

\restrict vLCQNoR2rbHNvPcbM6zblhfPJ2CKIsRITJTC9d3tlXBaBGyfMkpoh8E3TckuqEG

-- Dumped from database version 16.10
-- Dumped by pg_dump version 16.10

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
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id integer,
    media_id integer
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
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    creator_id integer NOT NULL,
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
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id integer
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
    favoritegenre character varying(255) DEFAULT 'Unknow'::character varying
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
-- Name: favorites favorites_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: media_entries media_entries_creator_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.media_entries
    ADD CONSTRAINT media_entries_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: rating_likes rating_likes_rating_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.rating_likes
    ADD CONSTRAINT rating_likes_rating_id_fkey FOREIGN KEY (rating_id) REFERENCES public.ratings(id) ON DELETE CASCADE;


--
-- Name: rating_likes rating_likes_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myuser
--

ALTER TABLE ONLY public.rating_likes
    ADD CONSTRAINT rating_likes_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


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

\unrestrict vLCQNoR2rbHNvPcbM6zblhfPJ2CKIsRITJTC9d3tlXBaBGyfMkpoh8E3TckuqEG

