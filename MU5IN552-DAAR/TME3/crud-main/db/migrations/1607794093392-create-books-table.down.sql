-- Your migration code here.
drop trigger if exists
  books_moddatetime on books;
drop table books;
