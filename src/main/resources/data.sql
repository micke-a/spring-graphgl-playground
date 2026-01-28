
insert into customer (name)
values
    ('john'),
    ('jane'),
    ('dude'),
    ('drake'),
    ('yolo');

insert into card (card_holder_id, card_type, account_id)
values
    (1, 'VIRTUAL', '1'),
    (1, 'PHYSICAL', '1'),
    (2, 'VIRTUAL', '2'),
    (2, 'PHYSICAL', '2'),
    (3, 'VIRTUAL', '3'),
    (3, 'PHYSICAL', '3'),
    (4, 'VIRTUAL', '4'),
    (4, 'PHYSICAL', '4'),
    (5, 'VIRTUAL', '5'),
    (5, 'PHYSICAL', '5');

insert into account (owner_id, account_type, balance)
values
    (1, 'CURRENT', 1000),
    (2, 'CURRENT', 1000),
    (3, 'CURRENT', 1000),
    (4, 'CURRENT', 1000),
    (5, 'CURRENT', 1000);