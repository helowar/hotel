package com.mangocity.util;

import java.util.*;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


/**
 * @author Joe Black.Lee
 * 
 */
public class RandomStrUtil {
    /**
     * Automatically generated variable: CONSTANT_LENGTH
     */
    private static final int CONSTANT_LENGTH = 30;

    /**
     * Automatically generated variable: CONSTANT_NUM
     */
    private static final int CONSTANT_NUM = 100;

    /**
     * Automatically generated variable: CONSTANT_FLOAT
     */
    private static final int CONSTANT_FLOAT = 93;

    /**
     * Automatically generated variable: CONSTANT_INT
     */
    private static final int CONSTANT_INT = 34;

    private Integer length = new Integer(8);

    public void setLength(int count) {
        length = new Integer(count);
    }

    private String randomstr;

    private boolean allchars = false;

    private Map hmap;

    private List lower = null;

    private List upper = null;

    private char[] single = null;

    private int singlecount = 0;

    private boolean singles = false;

    private String algorithm = null;

    private String provider = null;

    private boolean secure = false;

    private Random random = null;

    private final SecureRandom secrandom = null;

    private final float getFloat() {
        if (null == random)
            return secrandom.nextFloat();
        else
            return random.nextFloat();
    }

    /**
     * generate the Random object that will be used for this random number generator
     * 
     */
    public final void generateRandomObject() throws Exception {

        // check to see if the object is a SecureRandom object
        if (secure) {
            try {
                // get an instance of a SecureRandom object
                if (null != provider)
                    // search for algorithm in package provider
                    random = SecureRandom.getInstance(algorithm, provider);
                else
                    random = SecureRandom.getInstance(algorithm);
            } catch (NoSuchAlgorithmException ne) {
                throw new Exception(ne.getMessage());
            } catch (NoSuchProviderException pe) {
                throw new Exception(pe.getMessage());
            }
        } else
            random = new Random();
    }

    /**
     * generate the random string
     * 
     */
    private final void generaterandom() {

        if (allchars)
            for (int i = 0; i < length.intValue(); i++)
                randomstr += new Character(
                    DoConvert.trunIntToChar(CONSTANT_INT 
                        + (DoConvert.truncFloatToInt(getFloat() * CONSTANT_FLOAT))))
                        .toString();
        else if (singles) {
            // check if there are single chars to be included

            if (3 == upper.size()) {
                // check for the number of ranges max 3 uppercase lowercase
                // digits

                // build the random string
                for (int i = 0; i < length.intValue(); i++) {
                    // you have four groups to choose a random number from, to
                    // make
                    // the choice a little more random select a number out of
                    // 100

                    // get a random number even or odd
                    if (0 == ((int)(getFloat() * CONSTANT_NUM)) % 2) {

                        // the number was even get another number even or odd
                        if (0 == ((int)(getFloat() * CONSTANT_NUM)) % 2)
                            // choose a random char from the single char group
                            randomstr += randomSingle().toString();
                        else
                            // get a random char from the first range
                            randomstr += randomChar((Character) 
                                lower.get(2), 
                                (Character) upper.get(2)).toString();
                    } else {
                        // the number was odd

                        if (0 == ((int)(getFloat() * CONSTANT_NUM)) % 2)
                            // choose a random char from the second range
                            randomstr += randomChar(
                                (Character) lower.get(1), 
                                (Character) upper.get(1)).toString();
                        else
                            // choose a random char from the third range
                            randomstr += randomChar(
                                (Character) lower.get(0),
                                (Character) upper.get(0)).toString();
                    }
                }
            } else if (2 == upper.size()) {
                // single chars are to be included choose a random char from
                // two different ranges

                // build the random char from single chars and two ranges
                for (int i = 0; i < length.intValue(); i++) {
                    // select the single chars or a range to get each random
                    // char
                    // from

                    if (0 == ((int)(getFloat() * CONSTANT_NUM)) % 2) {

                        // get random char from the single chars
                        randomstr += randomSingle().toString();
                    } else if (0 == ((int)(getFloat() * CONSTANT_NUM)) % 2) {

                        // get the random char from the first range
                        randomstr += randomChar((Character) lower.get(1), (Character) upper.get(1))
                                .toString();
                    } else {

                        // get the random char from the second range
                        randomstr += randomChar((Character) lower.get(0), (Character) upper.get(0))
                                .toString();
                    }
                }
            } else if (1 == upper.size()) {

                // build the random string from single chars and one range
                for (int i = 0; i < length.intValue(); i++) {
                    if (0 == ((int)getFloat() * CONSTANT_NUM) % 2)
                        // get a random single char
                        randomstr += randomSingle().toString();
                    else
                        // get a random char from the range
                        randomstr += randomChar((Character) lower.get(0), (Character) upper.get(0))
                                .toString();
                }
            } else {
                // build the rand string from single chars
                for (int i = 0; i < length.intValue(); i++)
                    randomstr += randomSingle().toString();
            }
        } else {

            // no single chars are to be included in the random string
            if (3 == upper.size()) {

                // build random strng from three ranges
                for (int i = 0; i < length.intValue(); i++) {

                    if (0 == ((int)(getFloat() * CONSTANT_NUM)) % 2) {

                        // get random char from first range
                        randomstr += randomChar((Character) lower.get(2), (Character) upper.get(2))
                                .toString();
                    } else if (0 == ((int)(getFloat() * CONSTANT_NUM)) % 2) {

                        // get random char form second range
                        randomstr += randomChar((Character) lower.get(1), (Character) upper.get(1))
                                .toString();
                    } else {

                        // get random char from third range
                        randomstr += randomChar((Character) lower.get(0), (Character) upper.get(0))
                                .toString();
                    }
                }
            } else if (2 == upper.size()) {

                // build random string from two ranges
                for (int i = 0; i < length.intValue(); i++) {
                    if (0 == ((int)(getFloat() * CONSTANT_NUM)) % 2)
                        // get random char from first range
                        randomstr += randomChar((Character) lower.get(1), (Character) upper.get(1))
                                .toString();
                    else
                        // get random char from second range
                        randomstr += randomChar((Character) lower.get(0), (Character) upper.get(0))
                                .toString();
                }
            } else

                // build random string
                for (int i = 0; i < length.intValue(); i++)
                    // get random char from only range
                    randomstr += randomChar(
                        (Character) lower.get(0), 
                        (Character) upper.get(0)).toString();
        }
    }

    /**
     * generate a random char from the single char list
     * 
     * @return - a randomly selscted character from the single char list
     * 
     */
    private final Character randomSingle() {

        return new Character(single[DoConvert.truncFloatToInt((getFloat() * singlecount) - 1)]);
    }

    /**
     * generate a random character
     * 
     * @param lower
     *            lower bound from which to get a random char
     * @param upper
     *            upper bound from which to get a random char
     * 
     * @return - a randomly generated character
     * 
     */
    private final Character randomChar(Character lower, Character upper) {
        int tempval;
        char low = lower.charValue();
        char up = upper.charValue();

        // get a random number in the range lowlow - lowup
        tempval = DoConvert.truncFloatToInt((int) low + (getFloat() * ((up - low))));

        // return the random char
        return new Character (DoConvert.trunIntToChar(tempval));
    }

    /**
     * get the randomly created string for use with the &lt;jsp:getProperty name=<i>"id"</i>
     * property="randomstr"/&gt;
     * 
     * @return - randomly created string
     * 
     */
    public final String getRandom() {

        randomstr = new String();

        generaterandom(); // generate the first random string

        if (null != hmap) {

            while (hmap.containsKey(randomstr)) {
                // random string has already been created generate a different
                // one
                generaterandom();
            }

            hmap.put(randomstr, null); // add the new random string
        }

        return randomstr;
    }

    /**
     * set the ranges from which to choose the characters for the random string
     * 
     * @param low
     *            set of lower ranges
     * @param up
     *            set of upper ranges
     * 
     */
    public final void setRanges(List low, List up) {
        lower = low;
        upper = up;
    }

    /**
     * set the hashmap that is used to check the uniqueness of random strings
     * 
     * @param map
     *            hashmap whose keys are used to insure uniqueness of random strgs
     * 
     */
    public final void setHmap(Map map) {
        hmap = map;
    }

    /**
     * set the length of the random string
     * 
     * @param value
     *            length of the random string
     * 
     */
    public final void setLength(String value) {
        length = new Integer(value);

    }

    /**
     * set the algorithm name
     * 
     * @param value
     *            name of the algorithm to use for a SecureRandom object
     * 
     */
    public final void setAlgorithm(String value) {
        algorithm = value;
        secure = true; // a SecureRandom object is to be used
    }

    /**
     * set the provider name
     * 
     * @param value
     *            name of the package to check for the algorithm
     * 
     */
    public final void setProvider(String value) {
        provider = value;
    }

    /**
     * set the allchars flag
     * 
     * @param value
     *            boolean value of the allchars flag
     * 
     */
    public final void setAllchars(boolean value) {
        allchars = value;
    }

    /**
     * set the array of single chars to choose from for this random string and the number of chars
     * in the array
     * 
     * @param chars
     *            the array of single chars
     * @param value
     *            the number of single chars
     * 
     */
    public final void setSingle(char[] chars, int value) {
        single = chars; // set the array of chars
        singlecount = value; // set the number of chars in array single
        singles = true; // set flag that single chars are in use
    }

    public final void setCharset(String value) {
        // values tells the method whether or not to check for single chars
        boolean more = true;

        // create the arraylists to hold the upper and lower bounds for the char
        // ranges
        lower = new ArrayList(3);
        upper = new ArrayList(3);

        // user has chosen to use all possible characters in the random string
        if (0 == value.compareTo("all")) {
            allchars = true; // set allchars flag
            // all chars are to be used so there are no single chars to sort
            // through
            more = false;
        } else if (('-' == value.charAt(1)) && ('\\' != value.charAt(0))) {
            // run through the ranges at most 3
            while (more && ('-' == value.charAt(1))) {

                // check to make sure that the dash is not the single char
                if ('\\' == value.charAt(0))
                    break;
                else {
                    // add upper and lower ranges to there list
                    lower.add(new Character(value.charAt(0)));
                    upper.add(new Character(value.charAt(2)));
                }

                // check to see if there is more to the charset
                if (3 >= value.length())
                    more = false;
                else
                    // create a new string so that the next range if there is
                    // one
                    // starts it
                    value = value.substring(3);
            }
        }

        // if more = false there are no single chars in the charset
        if (more) {

            single = new char[CONSTANT_LENGTH]; // create single

            // create a set of tokens from the string of single chars
            StringTokenizer tokens = new StringTokenizer(value);

            while (tokens.hasMoreTokens()) {
                // get the next token from the string
                String token = tokens.nextToken();

                if (1 < token.length())
                    // char is a - add it to the list
                    single[singlecount++] = '-';

                // add the current char to the list
                single[singlecount++] = token.charAt(0);
            }
        }
        if ((null == lower) && (null == single))
            setCharset("a-zA-Z0-9");
    }

}
