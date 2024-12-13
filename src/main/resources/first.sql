--  SELECT positionCard.name FROM positionCard pc JOIN group g ON g.groupId = pc.groupId WHERE

create table "group" (
          groupId VARCHAR(200) UNIQUE NOT NULL,
          parentId VARCHAR(200),
          name VARCHAR(200) NOT NULL
      );

create table positionCard (
                 id VARCHAR(200) PRIMARY KEY,
                 isShowInApp BOOLEAN DEFAULT FALSE,
                 name VARCHAR(200),
                 groupId VARCHAR(200) NOT NULL UNIQUE
             );

insert into "group" (groupId, parentId, name) values ('1', null, 'group1');
insert into "group" (groupId, parentId, name) values ('2', '1', 'group2');
insert into "group" (groupId, parentId, name) values ('3', null, 'group3');
insert into "group" (groupId, parentId, name) values ('4', '3', 'group4');
insert into positionCard (id, isShowInApp, name, groupId) values ('1', true, 'card1', '1');
insert into positionCard (id, isShowInApp, name, groupId) values ('2', true, 'card2', '2');
insert into positionCard (id, isShowInApp, name, groupId) values ('3', true, 'card3', '3');
insert into positionCard (id, isShowInApp, name, groupId) values ('4', true, 'card4', '4');

WITH RECURSIVE groupStructure AS (
    SELECT g.groupId, g.parentId, g.name
    FROM "group" g
    WHERE g.groupId = :groupId

    UNION ALL

    SELECT g.groupId, g.parentId, g.name
    FROM "group" g
    JOIN groupStructure gs ON g.groupId = gs.parentId
)

SELECT pc.name
FROM groupStructure gs
LEFT JOIN positionCard pc ON gs.groupId = pc.groupId
WHERE pc.name IS NOT NULL
ORDER BY gs.parentId NULLS FIRST
LIMIT 1