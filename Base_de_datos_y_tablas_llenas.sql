--
-- PostgreSQL database dump
--

\restrict jQ0CcO2BAhW9V79cszLkq2S9wod2YZ00ZBCptfP6SXe1PoaxQ90OsXxcScJlPQF

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

-- Started on 2025-11-07 11:41:27

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE IF EXISTS "Gaming_League";
--
-- TOC entry 5094 (class 1262 OID 16807)
-- Name: Gaming_League; Type: DATABASE; Schema: -; Owner: developer
--

CREATE DATABASE "Gaming_League" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';


ALTER DATABASE "Gaming_League" OWNER TO developer;

\unrestrict jQ0CcO2BAhW9V79cszLkq2S9wod2YZ00ZBCptfP6SXe1PoaxQ90OsXxcScJlPQF
\connect "Gaming_League"
\restrict jQ0CcO2BAhW9V79cszLkq2S9wod2YZ00ZBCptfP6SXe1PoaxQ90OsXxcScJlPQF

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 5096 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 223 (class 1259 OID 16881)
-- Name: games; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.games (
    game_code integer NOT NULL,
    game_name character varying(50) NOT NULL,
    game_description character varying(150) NOT NULL,
    platform character varying(30) NOT NULL,
    category character varying(30) NOT NULL
);


ALTER TABLE public.games OWNER TO developer;

--
-- TOC entry 227 (class 1259 OID 16993)
-- Name: games_game_code_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

ALTER TABLE public.games ALTER COLUMN game_code ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.games_game_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 219 (class 1259 OID 16808)
-- Name: leagues; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.leagues (
    league_id integer NOT NULL,
    league_name character varying(30) NOT NULL,
    rank character varying(20) NOT NULL,
    category character varying(30) NOT NULL,
    league_duration character varying(50) NOT NULL
);


ALTER TABLE public.leagues OWNER TO developer;

--
-- TOC entry 226 (class 1259 OID 16936)
-- Name: leagues_game; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.leagues_game (
    league_id integer NOT NULL,
    game_code integer NOT NULL
);


ALTER TABLE public.leagues_game OWNER TO developer;

--
-- TOC entry 228 (class 1259 OID 17064)
-- Name: leagues_league_id_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

ALTER TABLE public.leagues ALTER COLUMN league_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.leagues_league_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 16891)
-- Name: matches; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.matches (
    match_id integer NOT NULL,
    game_code integer NOT NULL,
    player_1_id integer NOT NULL,
    player_2_id integer NOT NULL,
    match_date date NOT NULL,
    result_match character varying(50) NOT NULL,
    result_teams character varying(50) NOT NULL
);


ALTER TABLE public.matches OWNER TO developer;

--
-- TOC entry 229 (class 1259 OID 17065)
-- Name: matches_match_id_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

ALTER TABLE public.matches ALTER COLUMN match_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.matches_match_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 16818)
-- Name: players; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.players (
    player_id integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    gender "char" NOT NULL,
    address character varying(100) NOT NULL,
    telephone_number character varying(14) NOT NULL,
    email character varying(20) NOT NULL,
    age integer
);


ALTER TABLE public.players OWNER TO developer;

--
-- TOC entry 225 (class 1259 OID 16918)
-- Name: players_game_ranking; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.players_game_ranking (
    player_id integer NOT NULL,
    game_code integer NOT NULL,
    ranking integer NOT NULL
);


ALTER TABLE public.players_game_ranking OWNER TO developer;

--
-- TOC entry 230 (class 1259 OID 17066)
-- Name: players_player_id_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

ALTER TABLE public.players ALTER COLUMN player_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.players_player_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 222 (class 1259 OID 16855)
-- Name: team_players; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.team_players (
    team_id integer NOT NULL,
    player_id integer NOT NULL,
    date_from date NOT NULL,
    date_to date NOT NULL
);


ALTER TABLE public.team_players OWNER TO developer;

--
-- TOC entry 221 (class 1259 OID 16831)
-- Name: teams; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.teams (
    team_id integer NOT NULL,
    team_name character varying(50) NOT NULL,
    date_created date NOT NULL,
    date_disbanded date NOT NULL,
    number_members integer NOT NULL,
    users_name character varying(50) NOT NULL,
    wins integer NOT NULL,
    ties integer NOT NULL,
    defeats integer NOT NULL,
    created_by_player_id integer NOT NULL
);


ALTER TABLE public.teams OWNER TO developer;

--
-- TOC entry 231 (class 1259 OID 17067)
-- Name: teams_team_id_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

ALTER TABLE public.teams ALTER COLUMN team_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.teams_team_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 5080 (class 0 OID 16881)
-- Dependencies: 223
-- Data for Name: games; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (1, 'f', 'f', 'f', 'f');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (2, 'g', 'f', 'f', 'f');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (3, 'League of Legends', 'Juego competitivo de MOBA disponible en PC.', 'PC', 'MOBA');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (4, 'Valorant', 'Juego competitivo de Shooter disponible en PC.', 'PC', 'Shooter');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (5, 'FIFA 25', 'Juego competitivo de Deportes disponible en PlayStation.', 'PlayStation', 'Deportes');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (6, 'Counter-Strike 2', 'Juego competitivo de Shooter disponible en PC.', 'PC', 'Shooter');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (7, 'Fortnite', 'Juego competitivo de Battle Royale disponible en PC.', 'PC', 'Battle Royale');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (8, 'Call of Duty: Modern Warfare III', 'Juego competitivo de Shooter disponible en Xbox.', 'Xbox', 'Shooter');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (9, 'Dota 2', 'Juego competitivo de MOBA disponible en PC.', 'PC', 'MOBA');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (10, 'Overwatch 2', 'Juego competitivo de Shooter disponible en PC.', 'PC', 'Shooter');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (11, 'Apex Legends', 'Juego competitivo de Battle Royale disponible en PC.', 'PC', 'Battle Royale');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (12, 'PUBG', 'Juego competitivo de Battle Royale disponible en PC.', 'PC', 'Battle Royale');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (13, 'Rocket League', 'Juego competitivo de Deportes disponible en PC.', 'PC', 'Deportes');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (14, 'Street Fighter 6', 'Juego competitivo de Lucha disponible en PlayStation.', 'PlayStation', 'Lucha');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (15, 'Mortal Kombat 1', 'Juego competitivo de Lucha disponible en Xbox.', 'Xbox', 'Lucha');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (16, 'Tekken 8', 'Juego competitivo de Lucha disponible en PlayStation.', 'PlayStation', 'Lucha');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (17, 'NBA 2K25', 'Juego competitivo de Deportes disponible en PlayStation.', 'PlayStation', 'Deportes');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (18, 'Rainbow Six Siege', 'Juego competitivo de Shooter disponible en PC.', 'PC', 'Shooter');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (19, 'Smash Bros Ultimate', 'Juego competitivo de Lucha disponible en Nintendo.', 'Nintendo', 'Lucha');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (20, 'Splatoon 3', 'Juego competitivo de Shooter disponible en Nintendo.', 'Nintendo', 'Shooter');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (21, 'StarCraft II', 'Juego competitivo de Estrategia disponible en PC.', 'PC', 'Estrategia');
INSERT INTO public.games (game_code, game_name, game_description, platform, category) OVERRIDING SYSTEM VALUE VALUES (22, 'Minecraft', 'Juego competitivo de Sandbox disponible en PC.', 'PC', 'Sandbox');


--
-- TOC entry 5076 (class 0 OID 16808)
-- Dependencies: 219
-- Data for Name: leagues; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (1, 'Liga Champions 1', 'Bronce', 'MOBA', '5 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (2, 'Liga Nacional 2', 'Platino', 'Estrategia', '6 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (3, 'Liga Master 3', 'Platino', 'Estrategia', '5 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (4, 'Liga Nacional 4', 'Bronce', 'Lucha', '5 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (5, 'Liga Pro 5', 'Diamante', 'Lucha', '1 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (6, 'Liga Master 6', 'Diamante', 'Shooter', '1 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (7, 'Liga Elite 7', 'Oro', 'Shooter', '6 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (8, 'Liga Champions 8', 'Platino', 'Lucha', '4 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (9, 'Liga Pro 9', 'Diamante', 'MOBA', '3 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (10, 'Liga Nacional 10', 'Bronce', 'MOBA', '1 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (11, 'Liga Pro 11', 'Plata', 'Shooter', '3 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (12, 'Liga Elite 12', 'Diamante', 'MOBA', '2 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (13, 'Liga Nacional 13', 'Plata', 'Deportes', '1 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (14, 'Liga Master 14', 'Bronce', 'Shooter', '5 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (15, 'Liga Pro 15', 'Diamante', 'Estrategia', '4 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (16, 'Liga Pro 16', 'Plata', 'MOBA', '1 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (17, 'Liga Master 17', 'Bronce', 'Shooter', '2 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (18, 'Liga Elite 18', 'Bronce', 'Deportes', '2 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (19, 'Liga Master 19', 'Bronce', 'Lucha', '2 meses');
INSERT INTO public.leagues (league_id, league_name, rank, category, league_duration) OVERRIDING SYSTEM VALUE VALUES (20, 'Liga Champions 20', 'Diamante', 'MOBA', '1 meses');


--
-- TOC entry 5083 (class 0 OID 16936)
-- Dependencies: 226
-- Data for Name: leagues_game; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.leagues_game (league_id, game_code) VALUES (1, 1);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (2, 2);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (3, 3);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (4, 4);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (5, 5);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (6, 6);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (7, 7);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (8, 8);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (9, 9);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (10, 10);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (11, 11);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (12, 12);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (13, 13);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (14, 14);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (15, 15);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (16, 16);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (17, 17);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (18, 18);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (19, 19);
INSERT INTO public.leagues_game (league_id, game_code) VALUES (20, 20);


--
-- TOC entry 5081 (class 0 OID 16891)
-- Dependencies: 224
-- Data for Name: matches; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (1, 1, 14, 3, '2025-10-17', 'Victoria', 'Team B');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (2, 2, 12, 13, '2025-09-22', 'Empate', 'Empate');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (3, 3, 11, 7, '2025-09-08', 'Victoria', 'Team A');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (4, 4, 17, 14, '2025-09-19', 'Victoria', 'Team B');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (5, 5, 12, 16, '2025-09-11', 'Derrota', 'Team B');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (6, 6, 8, 16, '2025-06-01', 'Derrota', 'Team A');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (7, 7, 15, 12, '2025-07-14', 'Victoria', 'Empate');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (8, 8, 9, 11, '2025-04-23', 'Victoria', 'Team B');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (9, 9, 15, 7, '2025-06-25', 'Derrota', 'Team A');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (10, 10, 10, 7, '2025-06-03', 'Victoria', 'Team A');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (11, 11, 20, 5, '2025-03-08', 'Victoria', 'Team A');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (12, 12, 19, 1, '2025-03-19', 'Victoria', 'Team B');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (13, 13, 10, 18, '2025-09-06', 'Empate', 'Empate');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (14, 14, 7, 10, '2025-05-18', 'Victoria', 'Team A');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (15, 15, 8, 19, '2025-05-12', 'Victoria', 'Team B');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (16, 16, 18, 19, '2025-02-05', 'Victoria', 'Team B');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (17, 17, 9, 20, '2025-10-06', 'Derrota', 'Team B');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (18, 18, 9, 11, '2025-05-07', 'Derrota', 'Empate');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (19, 19, 6, 2, '2025-03-24', 'Victoria', 'Team B');
INSERT INTO public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result_match, result_teams) OVERRIDING SYSTEM VALUE VALUES (20, 20, 18, 6, '2025-04-15', 'Victoria', 'Empate');


--
-- TOC entry 5077 (class 0 OID 16818)
-- Dependencies: 220
-- Data for Name: players; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (1, 'Maria', 'Meza', 'F', 'Calle 37 #59, Hermosillo', '5388257736', 'maria.meza@gmail.com', 19);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (2, 'Hector', 'Moreno', 'M', 'Calle 15 #10, Hermosillo', '8553308217', 'hector.mo@gmail.com', 19);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (3, 'Sara', 'Flores', 'F', 'Calle 130 #25, Ciudad Guadalajara', '7879920788', 'sara.f@gmail.com', 19);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (4, 'Elizabeth', 'Bailey', 'O', 'Calle 174 #10, Virginia', '2125550189', 'elizabeth@gmail.com', 22);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (5, 'Yui', 'Sato', 'O', 'Calle 113 #68, Tokyo', '0698725432', 'yui.sato@gmail.com', 26);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (6, 'Asahi', 'Itō', 'O', 'Calle 132 #27, Kyoto', '07066663333', 'asahi@gmail.com', 20);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (7, 'Gregory', 'Violet', 'M', 'Calle 127 #74, Londres', '08007003000', 'gregory@gmail.com', 19);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (8, 'William', 'Moore', 'M', 'Calle 94 #95, Londres', '07888111222', 'liam.Mo@gmail.com', 25);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (9, 'Ana', 'Miller', 'F', 'Calle 57 #68, Houston', '71355510101', 'ana.m@gmail.com', 27);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (10, 'Houston', 'Wingfield', 'M', 'Calle 83 #26, Texas', '2145558888', 'houston@gmail.com', 28);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (11, 'Stanley', 'Snyder', 'M', 'Calle 184 #88, Texas', '9725551122', 'stanley@gmail.com', 28);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (12, 'Ethan', 'McKenzie', 'O', '14 Maple St., Toronto, ON', '165552291', 'ethan@gmail.com', 22);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (13, 'Isabela', 'Santos', 'O', 'Rua da Liberdade, 350, São Paulo', '11987654321', 'isabela@gmail.com', 29);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (14, 'Ana', 'Flores', 'F', 'Calle 34 #13, Ciudad México', '5528215147', 'ana@gmail.com', 31);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (15, 'Sofía', 'Ramírez', 'M', 'Calle 55 #46, Ciudad Puebla', '4356875624', 'sofía@gmail.com', 19);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (16, 'Charlotte', 'Miller', 'F', '7 Victoria Rd, London SW1A 0AA', '2079460999', 'charlotte@gmail.com', 29);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (17, 'Louis', 'Dubois', 'F', '5 Rue de la Paix, Paris', '612345678', 'louis@gmail.com', 31);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (18, 'Giulia', 'Rossi', 'F', 'Via Appia 45, Roma', '0698765432', 'giulia@gmail.com', 28);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (19, 'Carlos', 'Sánchez', 'F', 'Calle 76 #18, Ciudad Puebla', '5310811451', 'carlos@gmail.com', 28);
INSERT INTO public.players (player_id, first_name, last_name, gender, address, telephone_number, email, age) OVERRIDING SYSTEM VALUE VALUES (20, 'Kenji', 'Tanaka', 'O', '2-1-1 Ginza, Chuo-ku, Tokio', '9099991111', 'kenji@gmail.com', 34);


--
-- TOC entry 5082 (class 0 OID 16918)
-- Dependencies: 225
-- Data for Name: players_game_ranking; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (1, 1, 5);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (2, 2, 16);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (3, 3, 19);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (4, 4, 1);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (5, 5, 5);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (6, 6, 15);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (7, 7, 19);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (8, 8, 15);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (9, 9, 13);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (10, 10, 6);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (11, 11, 5);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (12, 12, 17);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (13, 13, 1);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (14, 14, 9);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (15, 15, 11);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (16, 16, 2);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (17, 17, 7);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (18, 18, 6);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (19, 19, 6);
INSERT INTO public.players_game_ranking (player_id, game_code, ranking) VALUES (20, 20, 3);


--
-- TOC entry 5079 (class 0 OID 16855)
-- Dependencies: 222
-- Data for Name: team_players; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (1, 5, '2024-02-10', '2024-11-29');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (2, 4, '2024-03-08', '2024-08-29');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (3, 19, '2024-10-26', '2025-05-13');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (4, 18, '2024-08-20', '2025-01-25');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (5, 20, '2024-01-22', '2024-07-19');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (6, 18, '2024-02-26', '2024-12-02');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (7, 7, '2024-09-03', '2025-03-06');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (8, 12, '2024-11-11', '2025-07-09');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (9, 13, '2024-10-09', '2025-03-18');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (10, 5, '2024-09-24', '2025-07-19');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (11, 2, '2024-06-13', '2024-11-12');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (12, 6, '2024-10-23', '2025-07-11');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (13, 5, '2024-02-16', '2024-07-22');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (14, 16, '2024-12-12', '2025-05-29');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (15, 1, '2024-07-07', '2025-02-16');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (16, 18, '2024-10-15', '2025-06-01');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (17, 3, '2024-02-13', '2024-07-20');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (18, 7, '2024-10-20', '2025-07-03');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (19, 20, '2024-03-25', '2024-12-08');
INSERT INTO public.team_players (team_id, player_id, date_from, date_to) VALUES (20, 15, '2024-03-13', '2024-09-23');


--
-- TOC entry 5078 (class 0 OID 16831)
-- Dependencies: 221
-- Data for Name: teams; Type: TABLE DATA; Schema: public; Owner: developer
--

INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (1, 'Equipo sisteFamily', '2024-02-22', '2025-02-14', 6, 'user_team1', 4, 2, 2, 17);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (2, 'Equipo ', '2024-08-22', '2025-06-29', 5, 'user_team2', 3, 2, 8, 16);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (3, 'Equipo Dragón3', '2024-09-07', '2025-05-02', 5, 'user_team3', 4, 5, 7, 7);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (4, 'Equipo Halcones4', '2024-07-28', '2025-08-21', 10, 'user_team4', 6, 4, 6, 8);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (5, 'Equipo Dragón5', '2024-05-18', '2025-02-17', 6, 'user_team5', 5, 2, 7, 3);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (6, 'Equipo Halcones6', '2024-03-01', '2024-12-09', 6, 'user_team6', 3, 4, 7, 6);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (7, 'Equipo Dragón7', '2024-06-07', '2025-06-01', 9, 'user_team7', 1, 0, 3, 2);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (8, 'Equipo Cobra8', '2024-12-14', '2025-08-30', 10, 'user_team8', 5, 3, 5, 8);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (9, 'Equipo Halcones9', '2024-06-25', '2025-07-19', 10, 'user_team9', 3, 0, 2, 10);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (10, 'Equipo Fénix10', '2024-06-19', '2025-01-30', 6, 'user_team10', 0, 3, 10, 16);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (11, 'Equipo Águilas11', '2024-09-18', '2025-07-20', 5, 'user_team11', 7, 3, 9, 14);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (12, 'Equipo Fénix12', '2024-04-16', '2024-11-20', 7, 'user_team12', 0, 3, 7, 13);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (13, 'Equipo Trueno13', '2024-04-03', '2025-03-05', 8, 'user_team13', 0, 1, 9, 14);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (14, 'Equipo Lince14', '2024-02-03', '2024-10-18', 7, 'user_team14', 7, 0, 4, 16);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (15, 'Equipo Trueno15', '2024-07-13', '2025-06-28', 7, 'user_team15', 1, 5, 3, 12);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (16, 'Equipo Águilas16', '2024-12-02', '2025-07-01', 9, 'user_team16', 2, 0, 6, 15);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (17, 'Equipo Cobra17', '2024-01-21', '2024-11-04', 5, 'user_team17', 8, 4, 6, 15);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (18, 'Equipo Cobra18', '2024-11-13', '2025-11-02', 5, 'user_team18', 10, 4, 10, 13);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (19, 'Equipo Lince19', '2024-05-28', '2025-04-12', 8, 'user_team19', 3, 2, 7, 10);
INSERT INTO public.teams (team_id, team_name, date_created, date_disbanded, number_members, users_name, wins, ties, defeats, created_by_player_id) OVERRIDING SYSTEM VALUE VALUES (20, 'Equipo Trueno20', '2024-08-12', '2025-06-24', 8, 'user_team20', 0, 3, 4, 8);


--
-- TOC entry 5097 (class 0 OID 0)
-- Dependencies: 227
-- Name: games_game_code_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.games_game_code_seq', 22, true);


--
-- TOC entry 5098 (class 0 OID 0)
-- Dependencies: 228
-- Name: leagues_league_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.leagues_league_id_seq', 20, true);


--
-- TOC entry 5099 (class 0 OID 0)
-- Dependencies: 229
-- Name: matches_match_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.matches_match_id_seq', 20, true);


--
-- TOC entry 5100 (class 0 OID 0)
-- Dependencies: 230
-- Name: players_player_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.players_player_id_seq', 20, true);


--
-- TOC entry 5101 (class 0 OID 0)
-- Dependencies: 231
-- Name: teams_team_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.teams_team_id_seq', 20, true);


--
-- TOC entry 4896 (class 2606 OID 16830)
-- Name: players PK_idPlayer; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players
    ADD CONSTRAINT "PK_idPlayer" PRIMARY KEY (player_id);


--
-- TOC entry 4888 (class 2606 OID 17073)
-- Name: players age_check; Type: CHECK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE public.players
    ADD CONSTRAINT age_check CHECK (((age > 18) AND (age <= 80))) NOT VALID;


--
-- TOC entry 4907 (class 2606 OID 16863)
-- Name: team_players datef_teamid_pID; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.team_players
    ADD CONSTRAINT "datef_teamid_pID" PRIMARY KEY (date_from, team_id, player_id);


--
-- TOC entry 4898 (class 2606 OID 16977)
-- Name: players email; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players
    ADD CONSTRAINT email UNIQUE (email) INCLUDE (email);


--
-- TOC entry 4910 (class 2606 OID 16890)
-- Name: games games_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.games
    ADD CONSTRAINT games_pkey PRIMARY KEY (game_code);


--
-- TOC entry 4889 (class 2606 OID 16970)
-- Name: players gender_check; Type: CHECK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE public.players
    ADD CONSTRAINT gender_check CHECK (((gender = 'M'::"char") OR (gender = 'F'::"char") OR (gender = 'O'::"char"))) NOT VALID;


--
-- TOC entry 4892 (class 2606 OID 16817)
-- Name: leagues id_PK; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues
    ADD CONSTRAINT "id_PK" PRIMARY KEY (league_id);


--
-- TOC entry 4903 (class 2606 OID 16845)
-- Name: teams id_team_PK; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.teams
    ADD CONSTRAINT "id_team_PK" PRIMARY KEY (team_id);


--
-- TOC entry 4918 (class 2606 OID 16942)
-- Name: leagues_game league_game_pk; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues_game
    ADD CONSTRAINT league_game_pk PRIMARY KEY (league_id, game_code);


--
-- TOC entry 4894 (class 2606 OID 16983)
-- Name: leagues league_name; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues
    ADD CONSTRAINT league_name UNIQUE (league_name) INCLUDE (league_name);


--
-- TOC entry 4914 (class 2606 OID 16902)
-- Name: matches match_id_pk; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT match_id_pk PRIMARY KEY (match_id);


--
-- TOC entry 4916 (class 2606 OID 16925)
-- Name: players_game_ranking player_game_pk; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players_game_ranking
    ADD CONSTRAINT player_game_pk PRIMARY KEY (player_id, game_code);


--
-- TOC entry 4890 (class 2606 OID 16988)
-- Name: players_game_ranking player_ranking; Type: CHECK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE public.players_game_ranking
    ADD CONSTRAINT player_ranking CHECK (((ranking >= 1) AND (ranking <= 20))) NOT VALID;


--
-- TOC entry 4900 (class 2606 OID 16979)
-- Name: players telephone_number; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players
    ADD CONSTRAINT telephone_number UNIQUE (telephone_number) INCLUDE (telephone_number);


--
-- TOC entry 4912 (class 2606 OID 16992)
-- Name: games unique_game_platform; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.games
    ADD CONSTRAINT unique_game_platform UNIQUE (game_name, platform);


--
-- TOC entry 4905 (class 2606 OID 16985)
-- Name: teams users_name; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.teams
    ADD CONSTRAINT users_name UNIQUE (users_name) INCLUDE (users_name);


--
-- TOC entry 4901 (class 1259 OID 16880)
-- Name: fki_created_by_id; Type: INDEX; Schema: public; Owner: developer
--

CREATE INDEX fki_created_by_id ON public.teams USING btree (created_by_player_id);


--
-- TOC entry 4908 (class 1259 OID 16874)
-- Name: fki_player_id; Type: INDEX; Schema: public; Owner: developer
--

CREATE INDEX fki_player_id ON public.team_players USING btree (player_id);


--
-- TOC entry 4927 (class 2606 OID 17029)
-- Name: leagues_game fk_lg_game; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues_game
    ADD CONSTRAINT fk_lg_game FOREIGN KEY (game_code) REFERENCES public.games(game_code) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4928 (class 2606 OID 17024)
-- Name: leagues_game fk_lg_league; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues_game
    ADD CONSTRAINT fk_lg_league FOREIGN KEY (league_id) REFERENCES public.leagues(league_id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4922 (class 2606 OID 17049)
-- Name: matches fk_matches_game; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT fk_matches_game FOREIGN KEY (game_code) REFERENCES public.games(game_code) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4923 (class 2606 OID 17068)
-- Name: matches fk_matches_p1; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT fk_matches_p1 FOREIGN KEY (player_1_id) REFERENCES public.players(player_id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4924 (class 2606 OID 17059)
-- Name: matches fk_matches_p2; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT fk_matches_p2 FOREIGN KEY (player_2_id) REFERENCES public.players(player_id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4925 (class 2606 OID 17019)
-- Name: players_game_ranking fk_pgr_game; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players_game_ranking
    ADD CONSTRAINT fk_pgr_game FOREIGN KEY (game_code) REFERENCES public.games(game_code) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4926 (class 2606 OID 17014)
-- Name: players_game_ranking fk_pgr_player; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players_game_ranking
    ADD CONSTRAINT fk_pgr_player FOREIGN KEY (player_id) REFERENCES public.players(player_id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4919 (class 2606 OID 17044)
-- Name: teams fk_team_created_by_player; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.teams
    ADD CONSTRAINT fk_team_created_by_player FOREIGN KEY (created_by_player_id) REFERENCES public.players(player_id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4920 (class 2606 OID 17039)
-- Name: team_players fk_tp_player; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.team_players
    ADD CONSTRAINT fk_tp_player FOREIGN KEY (player_id) REFERENCES public.players(player_id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4921 (class 2606 OID 17034)
-- Name: team_players fk_tp_team; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.team_players
    ADD CONSTRAINT fk_tp_team FOREIGN KEY (team_id) REFERENCES public.teams(team_id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 5095 (class 0 OID 0)
-- Dependencies: 5094
-- Name: DATABASE "Gaming_League"; Type: ACL; Schema: -; Owner: developer
--

REVOKE ALL ON DATABASE "Gaming_League" FROM developer;
GRANT CREATE,CONNECT ON DATABASE "Gaming_League" TO developer;
GRANT TEMPORARY ON DATABASE "Gaming_League" TO developer WITH GRANT OPTION;


-- Completed on 2025-11-07 11:41:27

--
-- PostgreSQL database dump complete
--

\unrestrict jQ0CcO2BAhW9V79cszLkq2S9wod2YZ00ZBCptfP6SXe1PoaxQ90OsXxcScJlPQF

