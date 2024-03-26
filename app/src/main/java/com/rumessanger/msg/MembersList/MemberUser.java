package com.rumessanger.msg.MembersList;

public class MemberUser {
        String username, profileImage, phone;

        public MemberUser(String username, String profileImage, String phone) {
            this.username = username;
            this.profileImage = profileImage;
            this.phone = phone;

        }

        public String getUsername() {
            return username;
        }

        public String getPhone() {
            return phone;
        }

        public String getAvatarUrl() {
            return profileImage;
        }
    }
