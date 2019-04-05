-- These scripts will be executed when the application boots
insert into route (id, departure_station, arrival_station, departure_time) values(1, 'Gent-Sint-Pieters', 'Brussel-Noord', '10:00:00.000');
insert into route (id, departure_station, arrival_station, departure_time) values(2, 'Brussel-Noord', 'Zottegem', '16:00:00.000');
insert into route (id, departure_station, arrival_station, departure_time) values(3, 'Zottegem', 'Brussel-Noord', '07:24:00.000');
insert into route (id, departure_station, arrival_station, departure_time) values(4, 'Dendermonde', 'Brussel-Noord', '07:24:00.000');

insert into delay (id, delay, departure_date_time, route_id, creation_date_time) values(1, 2, '2018-02-26T10:00:00.000Z', 1, '2018-02-26T09:00:00.000Z');
