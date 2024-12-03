GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO match_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO match_admin;
GRANT ALL PRIVILEGES ON SCHEMA public TO match_admin;

CREATE TRIGGER move_old_match_trigger
BEFORE INSERT ON match_result
FOR EACH ROW
EXECUTE FUNCTION move_old_match_trigger();

CREATE OR REPLACE FUNCTION move_old_match_trigger()
RETURNS TRIGGER AS $$
BEGIN
	IF EXISTS (
		SELECT 1 FROM match_result where match_id = NEW.match_id
	)
	THEN
	    -- Insert the row into the match_result_log table
	    INSERT INTO match_result_log (id, match_id, team_a, team_b, score_a, score_b, result_timestamp)
	    SELECT id, match_id, team_a, team_b, score_a, score_b, result_timestamp
	    FROM match_result
	    WHERE match_id = NEW.match_id;

		-- Delete the row from the match_result table
		DELETE FROM match_result
		WHERE match_id = NEW.match_id;
	END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TABLE match_result (
    id SERIAL PRIMARY KEY,
    match_id UUID NOT NULL,
    team_a VARCHAR(255),
    team_b VARCHAR(255),
    score_a INTEGER,
    score_b INTEGER,
    result_timestamp TIMESTAMP
);

CREATE TABLE match_result_log (
    id SERIAL PRIMARY KEY,
    match_id UUID NOT NULL,
    team_a VARCHAR(255),
    team_b VARCHAR(255),
    score_a INTEGER,
    score_b INTEGER,
    result_timestamp TIMESTAMP
);
