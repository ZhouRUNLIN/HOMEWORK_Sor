import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf(
                    "create table t_user_bookmark_%d\n" +
                            "(\n" +
                            "    id          bigint auto_increment primary key,\n" +
                            "    gid         varchar(20) not null,\n" +
                            "    username    varchar(50) not null,\n" +
                            "    bookId      bigint      not null,\n" +
                            "    create_time datetime    null comment 'creation time',\n" +
                            "    update_time datetime    null comment 'modified time',\n" +
                            "    del_flag    tinyint(1)  null comment 'Delete flag 0: not deleted 1: deleted'\n" +
                            ");\n",i);
        }
    }


}
