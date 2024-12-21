-- Your migration code here.create table books (
create table books (
  id uuid primary key default uuid_generate_v4(),
  info json,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp
);

create trigger books_moddatetime
  before update on books
  for each row
  execute procedure moddatetime (updated_at);
