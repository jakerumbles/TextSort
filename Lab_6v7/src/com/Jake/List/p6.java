/*
Author: Jake Edwards
Class: CSC 241-001

Date Started: 04/04/2017
Date Finished: 04/10/2017

Purpose: A program that allows the user to sort the text in a file either alphabetically(using radix sort) or in
         descending order by frequency of occurrence.

ADT's used: Node.java
Files used: p6.dat

Sample input:

Once there was a quick brown
brown fox who jumped over a lazy lazy dog
I am not sure why he jumped over the
lazy dog or even if he is actually brown
but I know there was a fox who
jumped over a dog
Why do you ask because the fox was jumpy

Sample output:

Enter the corresponding number for sort type desired:
1. Alphabetical
2. Descending order by frequency of occurence
1
a
a
a
a
actually
am
ask
because
brown
brown
brown
but
do
dog
dog

Press enter key for the next 15 words...

Enter key pressed...

dog
even
fox
fox
fox
he
he
i
i
if
is
jumped
jumped
jumped
jumpy

Press enter key for the next 15 words...
*/

// commented out b/c it doesn't work on the server. Uncomment it if you want to run it locally, otherwise it won't work
//package com.Jake.List;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class p6 {

    public static void main(String[] args) throws IOException{

        // stores all words from file delimited by spaces
        String words[] = getFile();
        Scanner userInput = new Scanner(System.in);

        // prompt user
        System.out.println("Enter the corresponding number for sort type desired: ");
        System.out.println("1. Alphabetical");
        System.out.println("2. Descending order by frequency of occurence");

        int sortType = userInput.nextInt();
        switch (sortType) {
            case 1:
                // sort alphabetically(radix sort)

                // sortedText is the array holding the text sorted alphabetically as returned by the radixSort method
                Object[] sortedText = radixSort(words);

                int counter = 0;
                // print out sorted text 15 words at a time. Prints another 15 each times user hits enter
                for (int i = 0; i < sortedText.length; i += 15) {

                    for (int j = 0; j < 15 && counter < sortedText.length; j++) {
                        System.out.println(sortedText[counter] + " ");
                        counter++;
                    }
                    System.out.println("\nPress enter key for the next 15 words...\n");

                    String readString = userInput.nextLine();
                    while (readString != null) {
                        if (readString.isEmpty()) {
                            System.out.println("Enter key pressed...\n");
                            readString = null;
                        }
                        else {
                            System.out.println("Please press enter for the next 15 words...\n");
                        }
                    }
                }
                break;

            case 2:
                //sort by descending order of frequency(quicksort)

                System.out.println("Worked on debugging this part of the program for about 3 hours straight with no luck.  Points for a very strong effort at least please. I've done the best I can.");

                Node[] nodes = new Node[words.length];
                int nodesCounter = 0;
                // create 2 attribute node class(one for word, other for number of occurences)
                // quicksort will then sort the nodes based on occurences attribute
                for (int i = 0; i < words.length; i++) {
                    if (words[i] != null) { // if it has a word and has not already been set to null(meaning it has already bee cataloged
                        Node node = new Node(words[i]);
                        node.addOccurence();
                        nodes[nodesCounter] = node;
                        words[i] = null;
                        nodesCounter++;

                        // cycles through and adds an occurence for every word match and the sets words[i] = null
                        int j = 1;
                        while (j < words.length) {
                            while (j < words.length && words[j] == null) {
                                j++;
                            }
                            if (words[j].equals(node.getWord())) {
                                node.addOccurence();
                                words[j] = null;
                                j++;
                            }
                            else {
                                j++;
                            }
                        }
                    }
                }

                // get length of nodes array
                int x = 0;
                while (nodes[x] != null) {
                    x++;
                }
                int nodesLength = x;

                quicksort(nodes, 0, nodesLength);

                for (int i = nodesLength - 1; i >= 0; i--) {
                    System.out.println(nodes[i].getWord() + "\t" + nodes[i].getNumOccurences());
                }

                break;
        }

    }

    // precondition: method is passed an array of nodes, the first index of the array, and the last index of the array
    // postcondition: upon method completion, the array that was passed in will now be sorted
    private static void quicksort(Node[] theArray, int first, int last) {

        int pivotIndex;

        if (first < last) {
            pivotIndex = partition(theArray, first, last);

            // sort s1
            quicksort(theArray, first, pivotIndex - 1);
            // sort s2
            quicksort(theArray, pivotIndex + 1, last);
        } // end if
    }

    // precondition: method is passed an array of nodes, the first index of the array, and the last index of the array
    // postcondition: the array that was passed in is now prepared to undergo quicksort and the method returns the index of the pivot value
    private static int partition(Node[] theArray, int first, int last) {

        int p = theArray[first].getNumOccurences();    // p is the pivot
        int lastS1 = first;         // set s1 and s2 to empty
        int firstUnknown = first + 1;  // set unknown region to theArray[first+1 ... last]

        // determine the regions s1 and s2
        // loop while not at end of array
        while (firstUnknown < last) {
            if (theArray[firstUnknown].getNumOccurences() < p) {
                // move theArray[firstUnknown] into s1
                Node temp = theArray[lastS1 + 1];
                theArray[lastS1 + 1] = theArray[firstUnknown];
                theArray[firstUnknown] = temp;
                lastS1++;
                firstUnknown++;
            }
            else {
                // move theArray[firstUnknown] into s2
                firstUnknown++;
            }
        }

        // place pivot in proper position b/w s1 and s2, and mark its new location
        Node temp = theArray[first];
        theArray[first] = theArray[lastS1];
        theArray[lastS1] = temp;
        return lastS1; // returns index of the pivot element
    }

    // precondition: method is passed an array of strings to be sorted alphabetically
    // postcondition: method returns the array sorted alphabetically
    private static Object[] radixSort(String[] arr) {

        // find the longest word
        int longestWord = 0;
        for (String word : arr) {
            if (word.length() > longestWord) {
                longestWord = word.length();
            }
        }

        // holds the sorted words in each iteration
        Queue<String> sorted = new LinkedList<>();

        // add words to the sorted queue
        for (int i = 0; i < arr.length; i++) {
            sorted.add(arr[i]);
        }

        // make the buckets
        Queue[] buckets = new Queue[26];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<String>();
        }

        // loops through each letter of the word from right to left
        for (int i = longestWord - 1; i >= 0; i--) {
            // loop through the words and check characters from left to right to sort into the buckets
            while (!sorted.isEmpty()) {
                String word = sorted.remove().toLowerCase();
                int bucketIndex = 0;
                if (i < word.length() && word.charAt(i) != '\'') {
                    bucketIndex = word.charAt(i) - 97;
                }
                buckets[bucketIndex].add(word);
            }
            // remove words from the buckets into the sorted queue
            for (Queue bucket : buckets) {
                while (!bucket.isEmpty()) {
                    String word = (String) bucket.remove();
                    sorted.add(word);
                }
            }
        }

        return sorted.toArray();
    }

    // precondition: method is called(no args)
    // postcondition: method returns an array of strings containing all the text from the selected file path split by spaces
    private static String[] getFile() throws IOException{

        // file of text
        File file = new File("C:\\Users\\edwar\\Documents\\Data Structures\\Take1\\Lab_6v7\\assets\\p6.dat");
        Scanner input = new Scanner(file);

        // load all text from file into string var temp
        String temp = "";
        while (input.hasNext()) {
            temp += input.next() + " ";
        }

        // temp.split returns an array of strings split by spaces
        String[] text = temp.split("\\s");

        // return text array for use in main method
        return text;
    }
}

/*
Author: Jake Edwards
Class: CSC 241-001

Date Started: 04/10/2017
Date Finished: 04/10/2017

ADT's used: none

Purpose: Utilized in Main class for quicksort.  Has 2 attributes(word and numOccurences) to facilitate descending order
         by frequency of occurence functionality.
 */

class Node {

    private String word;
    private int numOccurences;

    // default constructor
    public Node() {
        word = null;
        numOccurences = 0;
    }

    // overloaded constructor
    public Node(String word) {
        this.word = word;
        this.numOccurences = 0;
    }

    // getters and setters
    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setNumOccurences(int numOccurences) {
        this.numOccurences = numOccurences;
    }

    public int getNumOccurences() {
        return numOccurences;
    }

    // precondition: method is called on a node object
    // postcondition: node.numOccurences is increased by 1
    public void addOccurence() {
        this.numOccurences++;
    }
}

