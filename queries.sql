-- Verify submitted answers

SELECT *
FROM student_answer
WHERE quiz_attempt_id = 2
ORDER BY id;

-- Verify answer count

SELECT
    quiz_attempt_id,
    COUNT(*) AS total_answers
FROM student_answer
WHERE quiz_attempt_id = 2
GROUP BY quiz_attempt_id;


-- Verify quiz attempt status

SELECT
    id,
    student_id,
    quiz_id,
    started_at,
    submitted_at,
    status,
    score
FROM quiz_attempt
WHERE id = 2;


-- Verify answers with question details

SELECT
    sa.id,
    sa.quiz_attempt_id,
    sa.question_id,
    q.question_title,
    sa.selected_answer
FROM student_answer sa
JOIN question q ON sa.question_id = q.id
WHERE sa.quiz_attempt_id = 2
ORDER BY sa.question_id;