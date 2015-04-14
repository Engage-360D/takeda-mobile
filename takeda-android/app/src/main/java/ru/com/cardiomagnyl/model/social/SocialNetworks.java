package ru.com.cardiomagnyl.model.social;

// SocialNetwork Ids in ASNE
public class SocialNetworks {
    public static final int Twitter = 1;
    public static final int LinkedIn = 2;
    public static final int GooglePlus = 3;
    public static final int Facebook = 4;
    public static final int Vkontakte = 5;
    public static final int Odnoklassniki = 6;
    public static final int Instagram = 7;

    public static String getnameById(int socialId) {
        switch (socialId) {
            case Twitter:
                return "Twitter";
            case LinkedIn:
                return "LinkedIn";
            case GooglePlus:
                return "Google+";
            case Facebook:
                return "Facebook";
            case Vkontakte:
                return "Vkontakte";
            case Odnoklassniki:
                return "Odnoklassniki";
            case Instagram:
                return "Instagram";
            default:
                return "Unknown";
        }

    }

}