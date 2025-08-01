insert into customer(_id, name, surname, tckn) values(-2, 'Ing', 'Hubs', '37182000086');
insert into customer(_id, name, surname, tckn) values(-11, 'Gokhan', 'Tuna', '37183000086');
commit;

insert into wallet (_id, customer_id, wallet_name, currency, shopping_active, withdraw_active, balance, usable_balance) values (-3, '-2', 'test-wallet-2', 'USD', false, true, 500.12, 500.12);
insert into wallet (_id, customer_id, wallet_name, currency, shopping_active, withdraw_active, balance, usable_balance) values (-2, '-11', 'test-wallet-2', 'TRY', true, true, 1000.54, 1000.54);
insert into wallet (_id, customer_id, wallet_name, currency, shopping_active, withdraw_active, balance, usable_balance) values (-1, '-11', 'test-wallet-1', 'EUR', true, false, 30.98, 30.98);
commit;
