insert into card (card_holder, card_type, account_id)
values
    ('john', 'VIRTUAL', '1'),
    ('john', 'PHYSICAL', '1'),
    ('jane', 'VIRTUAL', '2'),
    ('jane', 'PHYSICAL', '2'),
    ('dude', 'VIRTUAL', '3'),
    ('dude', 'PHYSICAL', '3'),
    ('drake', 'VIRTUAL', '4'),
    ('drake', 'PHYSICAL', '4'),
    ('yolo', 'VIRTUAL', '5'),
    ('yolo', 'PHYSICAL', '5');

insert into account (owner, account_type, balance)
values
    ('john', 'CURRENT', 1000),
    ('jane', 'CURRENT', 1000),
    ('dude', 'CURRENT', 1000),
    ('drake', 'CURRENT', 1000),
    ('yolo', 'CURRENT', 1000);