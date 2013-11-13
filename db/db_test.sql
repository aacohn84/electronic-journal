CALL CREATE_USER('aacohn84', 'password', 'Aaron', 'Cohn');
CALL SET_TEACHER(1,TRUE);
CALL CREATE_GROUP(1, 1, 'group1', 'writing');
CALL CREATE_GROUP(1, 2, 'group2', 'social studies');
CALL CREATE_USER('stone13', 'password', 'Student', 'One');
CALL ADD_TO_ROSTER(1,2);
CALL ADD_TO_ROSTER(2,2);
CALL WRITE_PROMPT(1,'1st prompt');
CALL SAVE_RESPONSE(2,1,'response to 1st prompt');

