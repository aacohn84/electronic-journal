CALL CREATE_USER('aacohn84', 'password', 'Aaron', 'Cohn');

CALL CREATE_GROUP(1, 1, 'group1', 'testing');

CALL CREATE_GROUP(1, 2, 'group2', 'testing');

CALL CREATE_USER('stone13', 'password', 'Student', 'One');

CALL ADD_TO_ROSTER(1,2);

CALL ADD_TO_ROSTER(2,2);

CALL WRITE_PROMPT(1,'1st prompt');

CALL WRITE_PROMPT(1,'2nd prompt');

CALL SAVE_RESPONSE(2,1,'response to 1st prompt');

CALL SAVE_RESPONSE(2,2,'response to 2nd prompt');

CALL SAVE_RESPONSE(2,1,'edited response to 1st prompt');

CALL GET_GROUPS(1);

CALL GET_GROUPS(2);
