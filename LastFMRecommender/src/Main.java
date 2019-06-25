// Author       :   Alex Kourkoumelis
// Date         :   6/18/2019
// Title        :   LastFM Recommender
// Description  :   Loads user and artist information gathered by LastFM, and
//              :   utilizes graphs and various data structures to perform
//              :   operations on the data. Uses algs4 by Sedgewick.
//              :   Operations:
//              :   listFriends, commonFriends, listArtists, listTop10,
//              :   recommend10, listArtistByID, and listAllArtists


public class Main {

    public static void main(String[] args) {

        // dat files provided by LastFM
        String userFriendsDat = "user_friends.dat";
        String userArtistsDat = "user_artists.dat";
        String artistsDat = "artists.dat";

        Recommender recommender = new Recommender(userFriendsDat, userArtistsDat, artistsDat);

        recommender.listFriends(2);
        recommender.listFriends(31);
        recommender.listFriends(4);

        recommender.commonFriends(31, 4);

        recommender.commonFriends(1060, 500);

        recommender.commonFriends(12, 684);

        recommender.listArtists(31, 4);

        recommender.listTop10();

        recommender.recommend10(1283);

        recommender.listArtistByID(31);

//        recommender.listAllArtists();

    }
}
