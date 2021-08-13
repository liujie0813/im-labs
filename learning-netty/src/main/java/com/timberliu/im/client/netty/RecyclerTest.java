package com.timberliu.im.client.netty;

import io.netty.util.Recycler;

import javax.jws.soap.SOAPBinding;

/**
 * Created by liujie on 2021/7/2
 */

public class RecyclerTest {

    private static final Recycler<User> userRecycler = new Recycler() {
        @Override
        protected User newObject(Handle handle) {
            return new User(handle);
        }
    };

    static final class User {
        private String name;
        private Recycler.Handle<User> handle;
        public User(Recycler.Handle<User> handle) {
            this.handle = handle;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void recycler() {
            handle.recycle(this);
        }
    }

    public static void main(String[] args) {
        // 从对象池获取 User 对象
        User user = userRecycler.get();
        user.setName("liu");
        // 回收对象到对象池
        user.recycler();
        // 从对象池获取对象
        User user1 = userRecycler.get();
        System.out.println(user1.getName());
        System.out.println(user == user1);
    }
}
