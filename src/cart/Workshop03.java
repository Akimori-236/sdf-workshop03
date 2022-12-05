package cart;

import java.util.List;
import java.io.Console;
import java.util.ArrayList;
import java.util.LinkedList;

public class Workshop03 {
    public static void main(String[] args) {
        List<String> cart = new LinkedList<>();
        Console cons = System.console();
        Boolean stop = false;

        while (!stop) {
            String input = cons.readLine();
            input = input.trim().toLowerCase();
            // LIST
            String[] terms = input.split(" ");
            switch (terms[0]) {
                case "login":
                    // change to array for user
                    break;
                case "save":
                    // save cart of user
                    break;
                case "users":
                    // print list of users
                    break;
                case "list":
                    listCart(cart);
                    break;
                case "delete":
                    Integer indexDel = (Integer.parseInt(terms[1]));
                    if (indexDel > cart.size()) {
                        System.err.println("Incorrect item index");
                    } else {
                        System.out.printf("%s removed from cart\n", cart.get(indexDel - 1));
                        // cart.remove(indexDel);
                        cart.remove(indexDel - 1);
                    }
                    break;
                case "add":
                    input = input.replace(",", "");
                    for (int i = 1; i < terms.length; i++) {
                        if (cart.contains(terms[i])) {
                            System.err.printf("You have %s in your cart\n", terms[i]);
                        } else {
                            cart.add(terms[i]);
                            System.out.printf("%s added to cart\n", terms[i]);
                        }
                    }
                    break;
                case "stop":
                    stop = true;
                    break;
                default:
                    System.err.println("Invalid Command.");
            }
        }
    }

    public static ArrayList<String> getUsers() {
        ArrayList<String> userList = new ArrayList<>();
        return userList;
    }

    public static void listCart(List<String> cart) {
        if (cart.size() == 0) {
            System.out.println("Your cart is empty");
        } else {
            for (int i = 0; i < cart.size(); i++) {
                System.out.printf("%d. %s\n", (i + 1), cart.get(i));
            }
        }
    }
}