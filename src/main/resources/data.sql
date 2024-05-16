insert into gym (id, latitude, longitude, address, name, zip_code) values (1, 37.50416, 127.025142, '서울 강남구 강남대로 468', '클라이밍파크 신논현', null);
insert into gym (id, latitude, longitude, address, name, zip_code) values (2, 37.506311, 127.024755, '서울 강남구 강남대로118길 12', '락트리 클라이밍 강남', null);
insert into gym (id, latitude, longitude, address, name, zip_code) values (3, 37.485184, 127.035883, '서울 강남구 남부순환로 2615', '더클라임 양재점', null);

insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (1, 1, 1, '노랑', 'yellow');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (2, 2, 1, '핑크', 'pink');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (3, 3, 1, '파랑', 'blue');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (4, 4, 1, '빨강', 'red');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (5, 5, 1, '보라', 'purple');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (6, 6, 1, '갈색', 'brown');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (7, 7, 1, '회색', 'gray');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (8, 8, 1, '검정', 'black');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (9, 9, 1, '흰색', 'white');

insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (10, 1, 2, '빨강', 'red');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (11, 2, 2, '주황', 'orange');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (12, 3, 2, '노랑', 'yellow');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (13, 4, 1, '초록', 'green');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (14, 5, 2, '파랑', 'blue');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (15, 6, 2, '남색', 'navy');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (16, 7, 2, '보라', 'purple');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (17, 8, 2, '흰색', 'white');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (18, 9, 2, '검정', 'black');

insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (19, 1, 3, '흰색', 'white');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (20, 2, 3, '노랑', 'yellow');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (21, 3, 3, '주황', 'orange');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (22, 4, 3, '초록', 'green');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (23, 5, 3, '파랑', 'blue');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (24, 6, 3, '빨강', 'red');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (25, 7, 3, '보라', 'purple');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (26, 8, 3, '회색', 'gray');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (27, 9, 3, '갈색', 'brown');
insert into level (id, order_num, gym_id, color_name_ko, color_name_en) values (28, 10, 3, '검정', 'black');

insert into member (id, auth_id, provider_type, nickname, profile_url, email, home_gym_id, arm_span, height, weight) values(9999, 'test_auth_id_9999', 'kakao', 'test_nickname', 'http://k.kakaocdn.net/dn/dTDso6/btsECljbpYi/JqJl8DHkrVbuwYlBlVGEkK/img_110x110.jpg', null, null, null, null, null);

insert into record(id, member_id, level_id, gym_id, video_url, thumbnail_url, content, record_date) values(1, 9999, 1, 1, 'https://climingo-api.s3.ap-northeast-2.amazonaws.com/%EB%B9%84%EB%94%94%EC%98%A4_2024-05-16T12%3A40%3A26.647632061.MOV', 'https://climingo-api.s3.ap-northeast-2.amazonaws.com/%EC%8D%B8%EB%84%A4%EC%9D%BC_2024-05-16T21%3A35%3A57.247394.jpg', 'test_content', CURRENT_TIMESTAMP());
