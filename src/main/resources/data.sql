insert into gym (id, latitude, longitude, address, name, zip_code) values (1, 37.50416, 127.025142, '서울 강남구 강남대로 468', '클라이밍파크 신논현', null);
insert into gym (id, latitude, longitude, address, name, zip_code) values (2, 37.506311, 127.024755, '서울 강남구 강남대로118길 12', '락트리 클라이밍 강남', null);
insert into gym (id, latitude, longitude, address, name, zip_code) values (3, 37.485184, 127.035883, '서울 강남구 남부순환로 2615', '더클라임 양재점', null);

insert into grade (id, order_num, gym_id, color_name) values (1, 1, 1, '노랑');
insert into grade (id, order_num, gym_id, color_name) values (2, 2, 1, '핑크');
insert into grade (id, order_num, gym_id, color_name) values (3, 3, 1, '파랑');
insert into grade (id, order_num, gym_id, color_name) values (4, 4, 1, '빨강');
insert into grade (id, order_num, gym_id, color_name) values (5, 5, 1, '보라');
insert into grade (id, order_num, gym_id, color_name) values (6, 6, 1, '갈색');
insert into grade (id, order_num, gym_id, color_name) values (7, 7, 1, '회색');
insert into grade (id, order_num, gym_id, color_name) values (8, 8, 1, '검정');
insert into grade (id, order_num, gym_id, color_name) values (9, 9, 1, '흰색');

insert into grade (id, order_num, gym_id, color_name) values (10, 1, 2, '빨강');
insert into grade (id, order_num, gym_id, color_name) values (11, 2, 2, '주황');
insert into grade (id, order_num, gym_id, color_name) values (12, 3, 2, '노랑');
insert into grade (id, order_num, gym_id, color_name) values (13, 4, 1, '초록');
insert into grade (id, order_num, gym_id, color_name) values (14, 5, 2, '파랑');
insert into grade (id, order_num, gym_id, color_name) values (15, 6, 2, '남색');
insert into grade (id, order_num, gym_id, color_name) values (16, 7, 2, '보라');
insert into grade (id, order_num, gym_id, color_name) values (17, 8, 2, '흰색');
insert into grade (id, order_num, gym_id, color_name) values (18, 9, 2, '검정');

insert into grade (id, order_num, gym_id, color_name) values (19, 1, 3, '흰색');
insert into grade (id, order_num, gym_id, color_name) values (20, 2, 3, '노랑');
insert into grade (id, order_num, gym_id, color_name) values (21, 3, 3, '주황');
insert into grade (id, order_num, gym_id, color_name) values (22, 4, 3, '초록');
insert into grade (id, order_num, gym_id, color_name) values (23, 5, 3, '파랑');
insert into grade (id, order_num, gym_id, color_name) values (24, 6, 3, '빨강');
insert into grade (id, order_num, gym_id, color_name) values (25, 7, 3, '보라');
insert into grade (id, order_num, gym_id, color_name) values (26, 8, 3, '회색');
insert into grade (id, order_num, gym_id, color_name) values (27, 9, 3, '갈색');
insert into grade (id, order_num, gym_id, color_name) values (28, 10, 3, '검정');

insert into member (id, auth_id, provider_type, nickname, profile_image, email, home_gym_id, arm_span, height, weight) values(1, null, null, 'test', null, null, null, null, null, null);