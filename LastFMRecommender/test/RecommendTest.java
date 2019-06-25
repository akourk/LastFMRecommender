// Author       :   Alex Kourkoumelis
// Date         :   6/18/2019
// Title        :   LastFM Recommender
// Description  :   Loads user and artist information gathered by LastFM, and
//              :   utilizes graphs and various data structures to perform
//              :   operations on the data. Uses algs4 by Sedgewick.
//              :   Operations:
//              :   listFriends, commonFriends, listArtists, listTop10,
//              :   recommend10, listArtistByID, and listAllArtists

import edu.princeton.cs.algs4.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


public class RecommendTest {

    private static Digraph userFriendGraph;
    private static EdgeWeightedDigraph userArtistGraph;
    private static Map<Integer, String> artistID = new HashMap<>();

    // Constructor.
    public RecommendTest() {
        populateArtistID("artists.dat");

        userFriendGraph = new Digraph(findFileSize("user_friends.dat"));
        populateUserFriendGraph("user_friends.dat");

        userArtistGraph = new EdgeWeightedDigraph(findFileSize("user_artists.dat"));
        populateUserArtistGraph("user_artists.dat");
    }

    @Test
    public void listFriendsOf2() {
        String friendsOf2 = "1869 1625 1585 1327 1230 1210 1209 909 831 761 515 428 275 ";
        String adjOfUser = "";
        for (int friend : userFriendGraph.adj(2)) {
            adjOfUser += friend + " ";
        }
        assertEquals(friendsOf2, adjOfUser);
    }

    @Test
    public void listFriendsOf4() {
        String friendsOf4 = "2080 1103 850 534 520 493 264 211 124 99 ";
        String adjOfUser = "";
        for (int friend : userFriendGraph.adj(4)) {
            adjOfUser += friend + " ";
        }
        assertEquals(friendsOf4, adjOfUser);
    }

    @Test
    public void listFriendsOf31() {
        String friendsOf31 = "2097 1835 1653 1514 1343 1255 1213 1173 1164 1163 1081 954 859 762 658 575 " +
                "534 520 499 474 396 371 306 218 211 210 99 62 ";
        String adjOfUser = "";
        for (int friend : userFriendGraph.adj(31)) {
            adjOfUser += friend + " ";
        }
        assertEquals(friendsOf31, adjOfUser);
    }

    @Test
    public void commonFriendsOf31And4() {
        String commonFriends = "534 520 211 99 ";
        String adjOf31And4 = "";

        for (int friend1 : userFriendGraph.adj(31)) {
            for (int friend2 : userFriendGraph.adj(4)) {
                if (friend1 == friend2) {
                    adjOf31And4 += friend1 + " ";
                }
            }
        }

        assertEquals(commonFriends, adjOf31And4);
    }

    @Test
    public void commonFriendsOf1060And500() {
        String commonFriends = "440 ";
        String adjOf1060And500 = "";

        for (int friend1 : userFriendGraph.adj(1060)) {
            for (int friend2 : userFriendGraph.adj(500)) {
                if (friend1 == friend2) {
                    adjOf1060And500 += friend1 + " ";
                }
            }
        }

        assertEquals(commonFriends, adjOf1060And500);
    }

    @Test
    public void commonFriendsOf12And684() {
        String commonFriends = "534 520 211 99 ";
        String adjOf12And684 = "";

        for (int friend1 : userFriendGraph.adj(31)) {
            for (int friend2 : userFriendGraph.adj(4)) {
                if (friend1 == friend2) {
                    adjOf12And684 += friend1 + " ";
                }
            }
        }

        assertEquals(commonFriends, adjOf12And684);
    }

    @Test
    public void listArtists31And4() {
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(159);
        expected.add(77);
        expected.add(72);
        ArrayList<Integer> actual = new ArrayList<>();
        for (DirectedEdge x : userArtistGraph.adj(31)) {
            for (DirectedEdge y : userArtistGraph.adj(4)) {
                if (x.to() == y.to()) {
                    actual.add(x.to());
                }
            }
        }
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void listTop10() {
        ArrayList<Integer> expectedArtists = new ArrayList<>();
        ArrayList<Double> expectedListens = new ArrayList<>();
        expectedArtists.add(289);
        expectedListens.add(2393140.0);
        expectedArtists.add(72);
        expectedListens.add(1301308.0);
        expectedArtists.add(89);
        expectedListens.add(1291387.0);
        expectedArtists.add(292);
        expectedListens.add(1058405.0);
        expectedArtists.add(498);
        expectedListens.add(963449.0);
        expectedArtists.add(67);
        expectedListens.add(921198.0);
        expectedArtists.add(288);
        expectedListens.add(905423.0);
        expectedArtists.add(701);
        expectedListens.add(688529.0);
        expectedArtists.add(227);
        expectedListens.add(662116.0);
        expectedArtists.add(300);
        expectedListens.add(532545.0);

        ArrayList<Integer> actualArtist = new ArrayList<>();
        ArrayList<Double> actualListens = new ArrayList<>();

        Map<Integer, Double> weights = new HashMap<>();
        SortedMap<Double, Integer> weightSortedTreeMap = new TreeMap<>();

        for (int i = 1; i <= 2100; i++) {
            for (DirectedEdge e : userArtistGraph.adj(i)) {
                if (weights.containsKey(e.to())) {
                    double weight = e.weight();
                    weight += weights.get(e.to());
                    weights.put(e.to(), weight);
                } else {
                    weights.put(e.to(), e.weight());
                }
            }
        }

        for (Integer i : weights.keySet()) {
            weightSortedTreeMap.put(weights.get(i), i);
        }

        for(int i = 1; i < 11; i++) {
            actualArtist.add(weightSortedTreeMap.get(weightSortedTreeMap.lastKey()));
            actualListens.add(weightSortedTreeMap.lastKey());
            weightSortedTreeMap.remove(weightSortedTreeMap.lastKey());
        }

        assertArrayEquals(expectedArtists.toArray(), actualArtist.toArray());
        assertArrayEquals(expectedListens.toArray(), actualListens.toArray());
    }

    @Test
    public void recommend10for1283() {
        ArrayList<Integer> expectedArtists = new ArrayList<>();
        ArrayList<Double> expectedListens = new ArrayList<>();
        expectedArtists.add(289);
        expectedListens.add(250878.0);
        expectedArtists.add(378);
        expectedListens.add(212139.0);
        expectedArtists.add(89);
        expectedListens.add(113704.0);
        expectedArtists.add(344);
        expectedListens.add(99816.0);
        expectedArtists.add(461);
        expectedListens.add(84362.0);
        expectedArtists.add(498);
        expectedListens.add(81634.0);
        expectedArtists.add(681);
        expectedListens.add(60091.0);
        expectedArtists.add(458);
        expectedListens.add(55400.0);
        expectedArtists.add(288);
        expectedListens.add(55240.0);
        expectedArtists.add(292);
        expectedListens.add(51236.0);

        ArrayList<Integer> actualArtist = new ArrayList<>();
        ArrayList<Double> actualListens = new ArrayList<>();

        Map<Integer, Double> weights = new HashMap<>();
        SortedMap<Double, Integer> weightSortedTreeMap = new TreeMap<>();

        System.out.println("Top 10 Recommendations for user " + 1283 + " are: ");

        // grabbing all of users friends
        for (Integer id : userFriendGraph.adj(1283)) {
            for (DirectedEdge DE : userArtistGraph.adj(id)) {
                if (weights.containsKey(DE.to())) {
                    double weight = DE.weight();
                    weight += weights.get(DE.to());
                    weights.put(DE.to(), weight);
                } else {
                    weights.put(DE.to(), DE.weight());
                }
            }
        }

        for(DirectedEdge DE : userArtistGraph.adj(1283)) {
            if (weights.containsKey(DE.to())) {
                double weight = DE.weight();
                weight += weights.get(DE.to());
                weights.put(DE.to(), weight);
            } else {
                weights.put(DE.to(), DE.weight());
            }
        }

        for (Integer i : weights.keySet()) {
            weightSortedTreeMap.put(weights.get(i), i);
        }

        for(int i = 1; i < 11; i++) {
            actualArtist.add(weightSortedTreeMap.get(weightSortedTreeMap.lastKey()));
            actualListens.add(weightSortedTreeMap.lastKey());
            weightSortedTreeMap.remove(weightSortedTreeMap.lastKey());
        }

        assertArrayEquals(expectedArtists.toArray(), actualArtist.toArray());
        assertArrayEquals(expectedListens.toArray(), actualListens.toArray());
    }

    private static void populateArtistID(String fileName) {
        In in = new In(fileName);
        in.readLine();
        while(!in.isEmpty()) {
            String line = in.readLine();
            String[] split = line.split("\\t");
            artistID.put(Integer.parseInt(split[0]), split[1]);
        }
    }

    private static void populateUserArtistGraph(String fileName) {
        In in = new In(fileName);
        in.readLine();
        while (!in.isEmpty()) {
            int v = in.readInt();
            int w = in.readInt();
            int weight = in.readInt();
            DirectedEdge directedEdge = new DirectedEdge(v, w, weight);
            userArtistGraph.addEdge(directedEdge);
        }
    }

    private static void populateUserFriendGraph(String fileName) {
        In in = new In(fileName);
        in.readLine();
        while (!in.isEmpty()) {
            int v = in.readInt();
            int w = in.readInt();
            userFriendGraph.addEdge(v, w);
        }
    }

    private static int findFileSize(String fileName) {
        In in = new In(fileName);
        in.readLine();
        int fileSize = 0;
        while(!in.isEmpty()) {
            in.readLine();
            fileSize++;
        }
        return fileSize;
    }
}
