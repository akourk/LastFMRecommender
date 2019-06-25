// Author       :   Alex Kourkoumelis
// Date         :   6/18/2019
// Title        :   LastFM Recommender
// Description  :   Loads user and artist information gathered by LastFM, and
//              :   utilizes graphs and various data structures to perform
//              :   operations on the data. Uses algs4 by Sedgewick.
//              :   Operations:
//              :   listFriends, commonFriends, listArtists, listTop10,
//              :   recommend10, listArtistByID, and listAllArtists


import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Recommender {
    private static Digraph userFriendGraph;
    private static EdgeWeightedDigraph userArtistGraph;
    private static Map<Integer, String> artistID = new HashMap<>();

    // Constructor.
    public Recommender(String userFriendsDat, String userArtistsDat, String artistsDat) {

        populateArtistID(artistsDat);

        userFriendGraph = new Digraph(findFileSize(userFriendsDat));
        populateUserFriendGraph(userFriendsDat);

        userArtistGraph = new EdgeWeightedDigraph(findFileSize(userArtistsDat));
        populateUserArtistGraph(userArtistsDat);


    }

    // lists all friends of given user by iterating over .adj on a digraph.
    public void listFriends(int user) {
        System.out.println("Friends of user " + user + ":");
        for (int friend : userFriendGraph.adj(user)) {
            System.out.print(friend + " ");
        }
        System.out.println("\n");
    }

    // lists all common friends of 2 given users by iterating over 2 .adj in a nested foreach loop.
    public void commonFriends(int user1, int user2) {
        System.out.println("Common friends of " + user1 + " and " + user2 + ":");
        for (int friend1 : userFriendGraph.adj(user1)) {
            for (int friend2 : userFriendGraph.adj(user2)) {
                if (friend1 == friend2) {
                    System.out.print(friend1 + " ");
                }
            }
        }
        System.out.println("\n");
    }

    // lists all common artists listened to by 2 given users by checking .to() on directedEdges of
    // EdgeWeightedDigraphs of each user
    public void listArtists(int user1, int user2) {
        System.out.println("Artists listened to by both user " + user1 + " and " + user2 + ":");
        for (DirectedEdge x : userArtistGraph.adj(user1)) {
            for (DirectedEdge y : userArtistGraph.adj(user2)) {
                if (x.to() == y.to()) {
                    System.out.println(returnArtistByID(x.to()));
                }
            }
        }
        System.out.println("\n");
    }

    // lists top 10 artists listened to by all users by summing all the listens(weights) of each .to()
    // and then flipping id and weight and storing into a TreeMap, then printing the last element of
    // the TreeMap and deleting it 10 times.
    // both listTop10 and recommend10 use the same method, so it's been refactored into sortByWeight() and
    // printTop10().
    public void listTop10() {
        Map<Integer, Double> weights = new HashMap<>();
        SortedMap<Double, Integer> weightSortedTreeMap;

        System.out.println("The top 10 Artists listened to by all users are: ");
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

        weightSortedTreeMap = sortByWeight(weights);
        printTop10(weightSortedTreeMap);
    }

    // lists top 10 artists listened to by all of a users given friends and that user by summing all
    // the listens(weights) of each .to() and then flipping id and weight and storing into a TreeMap,
    // then printing the last element of the TreeMap and deleting it 10 times.
    // both listTop10 and recommend10 use the same method, so it's been refactored into sortByWeight() and
    // printTop10().
    public void recommend10(int user) {
        Map<Integer, Double> weights = new HashMap<>();
        SortedMap<Double, Integer> weightSortedTreeMap;

        System.out.println("Top 10 Recommendations for user " + user + " are: ");

        // grabbing all of users friends
        for (Integer id : userFriendGraph.adj(user)) {
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

        // adding weights from user's listens
        for(DirectedEdge DE : userArtistGraph.adj(user)) {
            if (weights.containsKey(DE.to())) {
                double weight = DE.weight();
                weight += weights.get(DE.to());
                weights.put(DE.to(), weight);
            } else {
                weights.put(DE.to(), DE.weight());
            }
        }

        weightSortedTreeMap = sortByWeight(weights);
        printTop10(weightSortedTreeMap);
    }

    // prints the artist associated with the given ID
    // utilizes the private method returnArtistByID
    public void listArtistByID(int id) {
        System.out.println("Artist ID: " + id + " corresponds to Artist: " + returnArtistByID(id));
    }

    // Sorts the passed Map by storing into a TreeMap, but flips the key and value.
    // used by listTop10 and recommend10.
    private SortedMap<Double, Integer> sortByWeight(Map<Integer, Double> weights) {
        SortedMap<Double, Integer> weightSortedTreeMap = new TreeMap<>();

        for (Integer i : weights.keySet()) {
            weightSortedTreeMap.put(weights.get(i), i);
        }

        return weightSortedTreeMap;
    }

    // prints the last element of the passed SortedMap and then deletes it, 10 times.
    // used by listTop10 and recommend10.
    private void printTop10(SortedMap<Double, Integer> weights) {

        for (int i = 1; i < 11; i++) {
            System.out.println(i + ": " + returnArtistByID(weights.get(weights.lastKey()))
                    + "\t\t\t\t" + weights.lastKey());
            weights.remove(weights.lastKey());
        }
        System.out.println("\n");
    }

    // private method that returns a String containing the artist associated with the passed ID
    private String returnArtistByID(int id) {
        return artistID.get(id);
    }

    public void listAllArtists() {
        for (Integer i : artistID.keySet()) {
            System.out.println(i + " " + artistID.get(i));
        }
    }

    // populates the artistID HashMap from the given file
    // used by the constructor
    private static void populateArtistID(String fileName) {
        In in = new In(fileName);
        in.readLine();
        while(!in.isEmpty()) {
            String line = in.readLine();
            String[] split = line.split("\\t");
            artistID.put(Integer.parseInt(split[0]), split[1]);
        }
    }

    // populates the userArtistGraph EdgeWeightedDigraph from the given file
    // used by the constructor
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

    // populates the userFriendGraph Digraph from the given file
    // used by the constructor
    private static void populateUserFriendGraph(String fileName) {
        In in = new In(fileName);
        in.readLine();
        while (!in.isEmpty()) {
            int v = in.readInt();
            int w = in.readInt();
            userFriendGraph.addEdge(v, w);
        }
    }

    // finds the file size of the passed file.
    // used by the constructor
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
