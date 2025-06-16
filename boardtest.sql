drop table board2 CASCADE CONSTRAINTS;--������ board2 ���̺� ����
drop table memberDTO cascade constraints;
drop sequence board2_seq; 
drop sequence memberDTO_seq -- �ڵ���ȣ ���� ���� 
--���̺� ����ص� ������ ���� �������� ��

create table board2(  --���̺� �����
bno number(5) primary key,
btitle nvarchar2(30) not null,
bcontent nvarchar2(1000) not null,
bwriter nvarchar2(10) not null,
bdate date not null
)

create sequence board2_seq increment by 1 start with 1 nocycle nocache

insert into board2 (bno, btitle, bcontent, bwriter, bdate) --���� ������ �Է�
values (board2_seq.nextval, '������~', '����� ��Ͻô���� ����ϼ̽��ϴ�', 'kkw', sysdate)
insert into board2 (bno, btitle, bcontent, bwriter, bdate)
values (board2_seq.nextval, '�ȳ��ϼ���~', '����� ��Ͻô���� ����ϼ̽��ϴ�', 'kbr', sysdate)
insert into board2 (bno, btitle, bcontent, bwriter, bdate)
values (board2_seq.nextval, '�����մϴ�~', '����� ��Ͻô���� ����ϼ̽��ϴ�', 'ksb', sysdate)
insert into board2 (bno, btitle, bcontent, bwriter, bdate)
values (board2_seq.nextval, '�����ϼ˳���~', '����� ��Ͻô���� ����ϼ̽��ϴ�', 'bhm', sysdate)
insert into board2 (bno, btitle, bcontent, bwriter, bdate)
values (board2_seq.nextval, 'ȭ�����ϼ���~', '����� ��Ͻô���� ����ϼ̽��ϴ�', 'lhn', sysdate)
insert into board2 (bno, btitle, bcontent, bwriter, bdate)
values (board2_seq.nextval, '�氩���ϴ�~', '����� ��Ͻô���� ����ϼ̽��ϴ�', 'jhj', sysdate)
insert into board2 (bno, btitle, bcontent, bwriter, bdate)
values (board2_seq.nextval, '�氩���ϴ�~', '����� ��Ͻô���� ����ϼ̽��ϴ�', 'kjw', sysdate)
insert into board2 (bno, btitle, bcontent, bwriter, bdate)
values (board2_seq.nextval, '�氩���ϴ�~', '����� ��Ͻô���� ����ϼ̽��ϴ�', 'jmk', sysdate)

select * from board2
-----------------------------------------------------------------------------------------------------------------------
--���� : 2�� ���̺��� �����Ͽ� ���� �����´�

select b.*, m.bwriter from memberDTO m inner join board2 b on m.id = b.bwriter;

--------------------------memberDTO ���̺� �θ�� �����ϱ�--------------------------------

DROP TABLE memberDTO
create table memberDTO(
mno number(5) not null,
bwriter nvarchar2 (10) not null,
id nvarchar2(10) primary key,
pw nvarchar2(10) not null,
balance int not null,
accountNum nvarchar2(7) not null,
regidate date default sysdate not null
)

create sequence memberDTO_seq increment by 1 start with 1 nocycle nocache


alter table board2 add constraint board2_memberDTO_fk foreign key (bwriter) references memberDTO(id)

insert into memberDTO (mno, bwriter, id, pw, accountNum, balance)
values (board2_seq.nextval, '����', 'kkw', '1234', memberDTO_seq.nextval, 50000)
insert into memberDTO (mno, bwriter, id, pw, accountNum, balance)
values (board2_seq.nextval, '�躸��', 'kbr', '1234', memberDTO_seq.nextval, 10000)
insert into memberDTO (mno, bwriter, id, pw, accountNum, balance)
values (board2_seq.nextval, '�����', 'ksb', '1234', memberDTO_seq.nextval, 10000)
insert into memberDTO (mno, bwriter, id, pw, accountNum, balance)
values (board2_seq.nextval, '������', 'bhm', '1234', memberDTO_seq.nextval, 10000)
insert into memberDTO (mno, bwriter, id, pw, accountNum, balance)
values (board2_seq.nextval, '���ϴ�', 'lhn', '1234', memberDTO_seq.nextval, 10000)
insert into memberDTO (mno, bwriter, id, pw, accountNum, balance)
values (board2_seq.nextval, '������', 'jhj', '1234', memberDTO_seq.nextval, 10000)
insert into memberDTO (mno, bwriter, id, pw, accountNum, balance)
values (board2_seq.nextval, '������', 'kjw', '1234', memberDTO_seq.nextval, 10000)
insert into memberDTO (mno, bwriter, id, pw, accountNum, balance)
values (board2_seq.nextval, '���α�', 'jmk', '1234', memberDTO_seq.nextval, 10000)



select * from memberDTO order by accountnum desc

