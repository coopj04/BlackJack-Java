import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.List;

class Blackjack {
    private final List <String> deck;

    public static Scanner sc = new Scanner(System.in);

    public static void clear() {System.out.println("\f");}

    public Blackjack(int numberOfDecks) {
        this.deck = new ArrayList<>();
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        // Create the deck
        for (int i = 0; i < numberOfDecks; i++) {
            for (String suit : suits) {
                for (String value : values) {
                    deck.add(value + " of " + suit);
                }
            }
        }

        // Shuffle the deck
        Collections.shuffle(deck);
    }

    // Draw a card from the deck
    public String drawCard() {
        return deck.remove(0);
    }

    // Get the value of a card
    public int getCardValue(String card, int aceValue) {
        String value = card.split(" ")[0];
        if (value.equals("A")) {
            return aceValue;
        } else if (value.equals("K") || value.equals("Q") || value.equals("J")) {
            return 10;
        } else {
            return Integer.parseInt(value);
        }
    }

    public int checkAceValue(int playerValue, int numberOfAces) {
        if (playerValue > 21 && numberOfAces > 0) {
            return 1;
        } else {
            return 11;
        }
    }

    // Global variable to keep track of the count
    static int count = 0;

    // Function to adjust the count based on the card value
    static void adjustCount(String card) {
        String value = card.split(" ")[0];
        if (value.equals("2") || value.equals("3") || value.equals("4") || value.equals("5") || value.equals("6")) {
            count++;
        } else if (value.equals("10") || value.equals("J") || value.equals("Q") || value.equals("K") || value.equals("A")) {
            count--;
        }
    }


    public static void main(String[] args) {
        // Create a new Blackjack game
        Blackjack game = new Blackjack(6);

        // Create a scanner to read input from the user
        Scanner scanner = new Scanner(System.in);

        // Player's starting money
        double playerMoney = 500;

        while (true) {
            //Clears previous round
            clear();

            int playerValue;
            int playerValue1 = 0;
            int playerValue2 = 0;
            int numberOfAces = 0;
            int dealerValue = 0;

            // Player bet amount
            System.out.println("Bet minimum is $25. You have $" + playerMoney + ". How much would you like to bet?");
            double betAmount = Double.parseDouble(scanner.nextLine());
            if (betAmount < 25) {
                System.out.println("Invalid amount. Please increase bet.");
            } else if (betAmount > playerMoney) {
                System.out.println("Insufficient funds. Please lower bet.");
            } else {
                System.out.println("You chose to bet " + betAmount + ". Good Luck!");
            }


            // Draw two cards for the player
            String playerCard1 = game.drawCard();
            adjustCount(playerCard1);
            String playerCard2 = game.drawCard();
            adjustCount(playerCard2);

            // Draw two cards for the dealer
            String dealerCard1 = game.drawCard();
            adjustCount(dealerCard1);
            String dealerCard2 = game.drawCard();
            adjustCount(dealerCard2);

            // Get the value of the player's hand
            playerValue1 += game.getCardValue(playerCard1, game.checkAceValue(playerValue1, numberOfAces));
            playerValue2 += game.getCardValue(playerCard2, game.checkAceValue(playerValue2, numberOfAces));
            playerValue = playerValue1 + playerValue2;
            if (game.getCardValue(playerCard1, game.checkAceValue(playerValue, numberOfAces)) == 11) numberOfAces++;
            if (game.getCardValue(playerCard1 + playerCard2, game.checkAceValue(playerValue, numberOfAces)) == 11) numberOfAces++;

            while (playerValue > 21 && numberOfAces > 0) {
                playerValue -= 10;
                numberOfAces--;
            }

            // Get the value of the dealer's hand
            dealerValue += game.getCardValue(dealerCard1, game.checkAceValue(dealerValue, numberOfAces));
            dealerValue += game.getCardValue(dealerCard2, game.checkAceValue(dealerValue, numberOfAces));
            if (game.getCardValue(dealerCard1, game.checkAceValue(dealerValue, numberOfAces)) == 11) numberOfAces++;
            if (game.getCardValue(dealerCard2, game.checkAceValue(dealerValue, numberOfAces)) == 11) numberOfAces++;

            while (dealerValue > 21 && numberOfAces > 0) {
                dealerValue -= 10;
                numberOfAces--;
            }

            // Print the player's cards
            System.out.println("Player's cards: " + playerCard1 + ", " + playerCard2 + " (" + playerValue + ")");

            // Print the dealer's cards
            System.out.println("Dealer's cards: " + dealerCard1 + ", [hidden]");

            while (true) {
                // Determine if player has Black Jack
                if (playerValue == 21) {
                    // Reveal the dealer's hidden card
                    System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                    if (playerValue == dealerValue) {
                        System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                    } else if (dealerValue < 21) {
                        betAmount *= 1.5;
                        playerMoney = playerMoney + betAmount;
                        System.out.println("You win! Total funds now at $" + playerMoney + ".");
                    }
                    break;
                }

                if (dealerValue == 21) {
                    playerMoney = playerMoney - betAmount;
                    // Reveal the dealer's hidden card
                    System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                    System.out.println("Dealer has Black Jack! You lose. Total funds now at $" + playerMoney + ".");
                    break;
                }

                //Ask the player if they want to split if eligible
                if (playerValue1 == playerValue2 && betAmount > playerMoney) {
                    System.out.println("Would you like to split? (y/n)");
                    String split = scanner.nextLine();
                    if (split.equalsIgnoreCase("y")) {
                        String playerCard3 = game.drawCard();
                        adjustCount(playerCard3);
                        playerValue1 += game.getCardValue(playerCard3, game.checkAceValue(playerValue1, numberOfAces));
                        if (game.getCardValue(playerCard3, game.checkAceValue(playerValue1, numberOfAces)) == 11) numberOfAces++;

                        System.out.println("Hand one: " + playerCard1 + ", " + playerCard3 + " (" + playerValue1 + ")");

                        //Determine if player had Black Jack
                        if (playerValue1 == 21) {
                            // Reveal the dealer's hidden card
                            System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                            if (dealerValue < 21) {
                                betAmount *= 1.5;
                                playerMoney = playerMoney + betAmount;
                                System.out.println("You win! Total funds now at $" + playerMoney + ".");
                            }

                        } else {
                            while (true) {
                                // Ask the player if they want to hit or stand
                                System.out.println("Do you want to hit, stand or double down? (h/s/d)");
                                String choice = scanner.nextLine();

                                if (choice.equalsIgnoreCase("h")) {
                                    String playerCard = game.drawCard();
                                    adjustCount(playerCard);
                                    playerValue1 += game.getCardValue(playerCard, game.checkAceValue(playerValue1, numberOfAces));
                                    if (game.getCardValue(playerCard, game.checkAceValue(playerValue1, numberOfAces)) == 11)
                                        numberOfAces++;
                                    while (playerValue1 > 21 && numberOfAces > 0) {
                                        playerValue1 -= 10;
                                        numberOfAces--;
                                    }
                                    System.out.println("Player drew: " + playerCard + " (" + playerValue1 + ")");
                                    if (playerValue1 > 21) {
                                        playerMoney = playerMoney - betAmount;
                                        System.out.println("Player bust! You lose. Total funds now at $" + playerMoney + ".");
                                        break;
                                    }

                                } else if (choice.equalsIgnoreCase("s")) {
                                    break;
                                } else if (choice.equalsIgnoreCase("d")) {
                                    if ((betAmount * 2) > playerMoney) {
                                        System.out.println("Insufficient funds. Please choose a different option.");
                                        break;
                                    }
                                    betAmount *= 2;
                                    String playerCard = game.drawCard();
                                    adjustCount(playerCard);
                                    playerValue1 += game.getCardValue(playerCard, game.checkAceValue(playerValue1, numberOfAces));
                                    if (game.getCardValue(playerCard, game.checkAceValue(playerValue1, numberOfAces)) == 11)
                                        numberOfAces++;
                                    while (playerValue1 > 21 && numberOfAces > 0) {
                                        playerValue1 -= 10;
                                        numberOfAces--;
                                    }
                                    System.out.println("Player drew: " + playerCard + " (" + playerValue1 + ")");
                                    if (playerValue1 > 21) {
                                        playerMoney = playerMoney - betAmount;
                                        System.out.println("Player bust! You lose. Total funds now at $" + playerMoney + ".");
                                        break;
                                    }

                                } else {
                                    System.out.println("Invalid choice. Please enter 'h' or 's' or 'd'.");
                                }
                            }
                        }


                        double betSplit = betAmount;
                        String playerCard4 = game.drawCard();
                        playerValue2 += game.getCardValue(playerCard4, game.checkAceValue(playerValue2, numberOfAces));
                        if (game.getCardValue(playerCard4, game.checkAceValue(playerValue2, numberOfAces)) == 11) numberOfAces++;

                        System.out.println("Hand two: " + playerCard2 + ", " + playerCard4 + " (" + playerValue2 + ")");

                        //Determine if player had Black Jack
                        if (playerValue2 == 21) {
                            // Reveal the dealer's hidden card
                            System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                            if (dealerValue < 21) {
                                betSplit *= 1.5;
                                playerMoney = playerMoney + betSplit;
                                System.out.println("You win! Total funds now at $" + playerMoney + ".");
                            }
                            break;
                        } else {
                            while (true) {
                                // Ask the player if they want to hit or stand
                                System.out.println("Do you want to hit, stand or double down? (h/s/d)");
                                String choice = scanner.nextLine();

                                if (choice.equalsIgnoreCase("h")) {
                                    String playerCard = game.drawCard();
                                    adjustCount(playerCard);
                                    playerValue2 += game.getCardValue(playerCard, game.checkAceValue(playerValue2, numberOfAces));
                                    if (game.getCardValue(playerCard, game.checkAceValue(playerValue2, numberOfAces)) == 11)
                                        numberOfAces++;
                                    while (playerValue2 > 21 && numberOfAces > 0) {
                                        playerValue2 -= 10;
                                        numberOfAces--;
                                    }
                                    System.out.println("Player drew: " + playerCard + " (" + playerValue2 + ")");
                                    if (playerValue2 > 21) {
                                        playerMoney = playerMoney - betSplit;
                                        System.out.println("Player bust! You lose. Total funds now at $" + playerMoney + ".");
                                        break;
                                    }
                                } else if (choice.equalsIgnoreCase("s")) {
                                    break;
                                } else if (choice.equalsIgnoreCase("d")) {
                                    if ((betSplit * 2) > playerMoney) {
                                        System.out.println("Insufficient funds. Please choose a different option.");
                                        break;
                                    }
                                    betSplit *= 2;
                                    String playerCard = game.drawCard();
                                    adjustCount(playerCard);
                                    playerValue2 += game.getCardValue(playerCard, game.checkAceValue(playerValue2, numberOfAces));
                                    if (game.getCardValue(playerCard, game.checkAceValue(playerValue2, numberOfAces)) == 11)
                                        numberOfAces++;
                                    while (playerValue2 > 21 && numberOfAces > 0) {
                                        playerValue -= 10;
                                        numberOfAces--;
                                    }
                                    System.out.println("Player drew: " + playerCard + " (" + playerValue2 + ")");
                                    if (playerValue2 > 21) {
                                        playerMoney = playerMoney - betSplit;
                                        System.out.println("Player bust! You lose. Total funds now at $" + playerMoney + ".");
                                        break;
                                    }
                                } else {
                                    System.out.println("Invalid choice. Please enter 'h' or 's' or 'd'.");
                                }
                            }
                        }

                        System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                        // Dealer hits until their value is at least 17
                        while (dealerValue < 17) {
                            String dealerCard = game.drawCard();
                            adjustCount(dealerCard);
                            dealerValue += game.getCardValue(dealerCard, game.checkAceValue(dealerValue, numberOfAces));
                            if (game.getCardValue(dealerCard, game.checkAceValue(dealerValue, numberOfAces)) == 11)
                                numberOfAces++;
                            while (dealerValue > 21 && numberOfAces > 0) {
                                dealerValue -= 10;
                                numberOfAces--;
                            }
                            System.out.println("Dealer drew: " + dealerCard + " (" + dealerValue + ")");
                        }
                        // Determine the winner for hand one
                        if (dealerValue > 21) {
                            playerMoney = playerMoney + betAmount;
                            System.out.println("Dealer bust! You win $" + betAmount + ". Total funds now at $" + playerMoney + ".");
                        } else if (playerValue1 > dealerValue) {
                            playerMoney = playerMoney + betAmount;
                            System.out.println("You win $" + betAmount + "! Total funds now at $" + playerMoney + ".");
                        } else if (playerValue1 < dealerValue) {
                            playerMoney = playerMoney - betAmount;
                            System.out.println("You lose. Total funds now at $" + playerMoney + ".");
                        } else {
                            System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                        }

                        //Determine the winner for hand two
                        if (dealerValue > 21) {
                            playerMoney = playerMoney + betSplit;
                            System.out.println("Dealer bust! You win $" + betSplit + ". Total funds now at $" + playerMoney + ".");
                        } else if (playerValue2 > dealerValue) {
                            playerMoney = playerMoney + betSplit;
                            System.out.println("You win $" + betSplit + "! Total funds now at $" + playerMoney + ".");
                        } else if (playerValue1 < dealerValue) {
                            playerMoney = playerMoney - betSplit;
                            System.out.println("You lose. Total funds now at $" + playerMoney + ".");
                        } else {
                            System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                        }
                        break;
                    } else if (split.equalsIgnoreCase("n")) {
                        // Ask the player if they want to hit or stand
                        System.out.println("Do you want to hit, stand or double down? (h/s/d)");
                        String choice = scanner.nextLine();

                        if (choice.equalsIgnoreCase("h")) {
                            String playerCard = game.drawCard();
                            adjustCount(playerCard);
                            playerValue += game.getCardValue(playerCard, game.checkAceValue(playerValue, numberOfAces));
                            if (game.getCardValue(playerCard, game.checkAceValue(playerValue, numberOfAces)) == 11)
                                numberOfAces++;
                            while (playerValue > 21 && numberOfAces > 0) {
                                playerValue -= 10;
                                numberOfAces--;
                            }
                            System.out.println("Player drew: " + playerCard + " (" + playerValue + ")");
                            if (playerValue > 21) {
                                playerMoney = playerMoney - betAmount;
                                System.out.println("Player bust! You lose. Total funds now at $" + playerMoney + ".");
                                // Reveal the dealer's hidden card
                                System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                                break;
                            }

                            if (playerValue == 21) {
                                // Reveal the dealer's hidden card
                                System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");

                                // Dealer hits until their value is at least 17
                                while (dealerValue < 17) {
                                    String dealerCard = game.drawCard();
                                    adjustCount(dealerCard);
                                    dealerValue += game.getCardValue(dealerCard, game.checkAceValue(playerValue, numberOfAces));
                                    System.out.println("Dealer drew: " + dealerCard + " (" + dealerValue + ")");
                                }

                                // Determine the winner
                                if (dealerValue > 21) {
                                    playerMoney = playerMoney + betAmount;
                                    System.out.println("Dealer bust! You win $" + betAmount + ".");
                                } else if (playerValue > dealerValue) {
                                    playerMoney = playerMoney + betAmount;
                                    System.out.println("You win $" + betAmount + "! Total funds now at $" + playerMoney + ".");
                                }else {
                                    System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                                }
                                break;
                            }
                        } else if (choice.equalsIgnoreCase("s")) {
                            // Reveal the dealer's hidden card
                            System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");

                            // Dealer hits until their value is at least 17
                            while (dealerValue < 17) {
                                String dealerCard = game.drawCard();
                                adjustCount(dealerCard);
                                dealerValue += game.getCardValue(dealerCard, game.checkAceValue(dealerValue, numberOfAces));
                                if (game.getCardValue(dealerCard, game.checkAceValue(dealerValue, numberOfAces)) == 11)
                                    numberOfAces++;
                                while (dealerValue > 21 && numberOfAces > 0) {
                                    dealerValue -= 10;
                                    numberOfAces--;
                                }
                                System.out.println("Dealer drew: " + dealerCard + " (" + dealerValue + ")");
                            }

                            // Determine the winner
                            if (dealerValue > 21) {
                                playerMoney = playerMoney + betAmount;
                                System.out.println("Dealer bust! You win $" + betAmount + ". Total funds now at $" + playerMoney + ".");
                            } else if (playerValue > dealerValue) {
                                playerMoney = playerMoney + betAmount;
                                System.out.println("You win $" + betAmount + "! Total funds now at $" + playerMoney + ".");
                            } else if (playerValue < dealerValue) {
                                playerMoney = playerMoney - betAmount;
                                System.out.println("You lose. Total funds now at $" + playerMoney + ".");
                            } else {
                                System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                            }
                            break;
                        } else if (choice.equalsIgnoreCase("d")) {
                            if ((betAmount * 2) > playerMoney) {
                                System.out.println("Insufficient funds. Please choose a different option.");
                                break;
                            }
                            betAmount *= 2;
                            String playerCard = game.drawCard();
                            adjustCount(playerCard);
                            playerValue += game.getCardValue(playerCard, game.checkAceValue(playerValue, numberOfAces));
                            if (game.getCardValue(playerCard, game.checkAceValue(playerValue, numberOfAces)) == 11)
                                numberOfAces++;
                            while (playerValue > 21 && numberOfAces > 0) {
                                playerValue -= 10;
                                numberOfAces--;
                            }
                            System.out.println("Player drew: " + playerCard + " (" + playerValue + ")");
                            // Reveal the dealer's hidden card
                            System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");

                            //Determine the winner
                            if (playerValue > 21) {
                                playerMoney = playerMoney - betAmount;
                                System.out.println("Player bust! You lose. Total funds now at $" + playerMoney + ".");
                                // Reveal the dealer's hidden card
                                System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                                break;
                            }

                            if (playerValue == 21) {
                                // Reveal the dealer's hidden card
                                System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");

                                // Dealer hits until their value is at least 17
                                while (dealerValue < 17) {
                                    String dealerCard = game.drawCard();
                                    adjustCount(dealerCard);
                                    dealerValue += game.getCardValue(dealerCard, game.checkAceValue(playerValue, numberOfAces));
                                    System.out.println("Dealer drew: " + dealerCard + " (" + dealerValue + ")");
                                }

                                // Determine the winner
                                if (dealerValue > 21) {
                                    playerMoney = playerMoney + betAmount;
                                    System.out.println("Dealer bust! You win $" + betAmount + ". Total funds now at $" + playerMoney + ".");
                                } else if (playerValue > dealerValue) {
                                    playerMoney = playerMoney + betAmount;
                                    System.out.println("You win $" + betAmount + "! Total funds now at $" + playerMoney + ".");
                                } else {
                                    System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                                }
                                break;
                            }

                            if (dealerValue > 21) {
                                playerMoney = playerMoney + betAmount;
                                System.out.println("Dealer bust! You win $" + betAmount + ". Total funds now at $" + playerMoney + ".");
                            } else if (playerValue > dealerValue) {
                                playerMoney = playerMoney + betAmount;
                                System.out.println("You win $" + betAmount + "! Total funds now at $" + playerMoney + ".");
                            } else if (playerValue < dealerValue) {
                                playerMoney = playerMoney - betAmount;
                                System.out.println("You lose. Total funds now at $" + playerMoney + ".");
                            } else {
                                System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                            }
                            break;
                        } else {
                            System.out.println("Invalid choice. Please enter 'h' or 's' or 'd'.");
                        }
                    }
                }


                // Ask the player if they want to hit or stand
                System.out.println("Do you want to hit, stand or double down? (h/s/d)");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("h")) {
                    String playerCard = game.drawCard();
                    adjustCount(playerCard);
                    playerValue += game.getCardValue(playerCard, game.checkAceValue(playerValue, numberOfAces));
                    if (game.getCardValue(playerCard, game.checkAceValue(playerValue, numberOfAces)) == 11)
                        numberOfAces++;
                    while (playerValue > 21 && numberOfAces > 0) {
                        playerValue -= 10;
                        numberOfAces--;
                    }
                    System.out.println("Player drew: " + playerCard + " (" + playerValue + ")");
                    if (playerValue > 21) {
                        playerMoney = playerMoney - betAmount;
                        System.out.println("Player bust! You lose. Total funds now at $" + playerMoney + ".");
                        // Reveal the dealer's hidden card
                        System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                        break;
                    }

                    if (playerValue == 21) {
                        // Reveal the dealer's hidden card
                        System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");

                        // Dealer hits until their value is at least 17
                        while (dealerValue < 17) {
                            String dealerCard = game.drawCard();
                            adjustCount(dealerCard);
                            dealerValue += game.getCardValue(dealerCard, game.checkAceValue(playerValue, numberOfAces));
                            System.out.println("Dealer drew: " + dealerCard + " (" + dealerValue + ")");
                        }

                        // Determine the winner
                       if (dealerValue > 21) {
                            playerMoney = playerMoney + betAmount;
                            System.out.println("Dealer bust! You win $" + betAmount + ".");
                        } else if (playerValue > dealerValue) {
                            playerMoney = playerMoney + betAmount;
                            System.out.println("You win $" + betAmount + "! Total funds now at $" + playerMoney + ".");
                        }else {
                            System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                        }
                        break;
                    }
                } else if (choice.equalsIgnoreCase("s")) {
                    // Reveal the dealer's hidden card
                    System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");

                    // Dealer hits until their value is at least 17
                    while (dealerValue < 17) {
                        String dealerCard = game.drawCard();
                        adjustCount(dealerCard);
                        dealerValue += game.getCardValue(dealerCard, game.checkAceValue(dealerValue, numberOfAces));
                        if (game.getCardValue(dealerCard, game.checkAceValue(dealerValue, numberOfAces)) == 11)
                            numberOfAces++;
                        while (dealerValue > 21 && numberOfAces > 0) {
                            dealerValue -= 10;
                            numberOfAces--;
                        }
                        System.out.println("Dealer drew: " + dealerCard + " (" + dealerValue + ")");
                    }

                    // Determine the winner
                    if (dealerValue > 21) {
                        playerMoney = playerMoney + betAmount;
                        System.out.println("Dealer bust! You win $" + betAmount + ". Total funds now at $" + playerMoney + ".");
                    } else if (playerValue > dealerValue) {
                        playerMoney = playerMoney + betAmount;
                        System.out.println("You win $" + betAmount + "! Total funds now at $" + playerMoney + ".");
                    } else if (playerValue < dealerValue) {
                        playerMoney = playerMoney - betAmount;
                        System.out.println("You lose. Total funds now at $" + playerMoney + ".");
                    } else {
                        System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                    }
                    break;
                } else if (choice.equalsIgnoreCase("d")) {
                    betAmount *= 2;
                    String playerCard = game.drawCard();
                    adjustCount(playerCard);
                    playerValue += game.getCardValue(playerCard, game.checkAceValue(playerValue, numberOfAces));
                    if (game.getCardValue(playerCard, game.checkAceValue(playerValue, numberOfAces)) == 11)
                        numberOfAces++;
                    while (playerValue > 21 && numberOfAces > 0) {
                        playerValue -= 10;
                        numberOfAces--;
                    }
                    System.out.println("Player drew: " + playerCard + " (" + playerValue + ")");

                    //Determine the winner
                    if (playerValue > 21) {
                        playerMoney = playerMoney - betAmount;
                        System.out.println("Player bust! You lose. Total funds now at $" + playerMoney + ".");
                        // Reveal the dealer's hidden card
                        System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                        break;
                    }

                    if (playerValue == 21) {
                        // Reveal the dealer's hidden card
                        System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");

                        // Dealer hits until their value is at least 17
                        while (dealerValue < 17) {
                            String dealerCard = game.drawCard();
                            adjustCount(dealerCard);
                            dealerValue += game.getCardValue(dealerCard, game.checkAceValue(playerValue, numberOfAces));
                            System.out.println("Dealer drew: " + dealerCard + " (" + dealerValue + ")");
                        }



                        // Determine the winner
                        if (dealerValue > 21) {
                            playerMoney = playerMoney + betAmount;
                            System.out.println("Dealer bust! You win $" + betAmount + ". Total funds now at $" + playerMoney + ".");
                        } else if (playerValue > dealerValue) {
                            playerMoney = playerMoney + betAmount;
                            System.out.println("You win $" + betAmount + "! Total funds now at $" + playerMoney + ".");
                        } else {
                            System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                        }
                        break;
                    }

                    // Reveal the dealer's hidden card
                    System.out.println("Dealer's cards: " + dealerCard1 + ", " + dealerCard2 + " (" + dealerValue + ")");
                    // Dealer hits until their value is at least 17
                    while (dealerValue < 17) {
                        String dealerCard = game.drawCard();
                        adjustCount(dealerCard);
                        dealerValue += game.getCardValue(dealerCard, game.checkAceValue(playerValue, numberOfAces));
                        System.out.println("Dealer drew: " + dealerCard + " (" + dealerValue + ")");
                    }

                    if (dealerValue > 21) {
                        playerMoney = playerMoney + betAmount;
                        System.out.println("Dealer bust! You win $" + betAmount + ". Total funds now at $" + playerMoney + ".");
                    } else if (playerValue > dealerValue) {
                        playerMoney = playerMoney + betAmount;
                        System.out.println("You win $" + betAmount + "! Total funds now at $" + playerMoney + ".");
                    } else if (playerValue < dealerValue) {
                        playerMoney = playerMoney - betAmount;
                        System.out.println("You lose. Total funds now at $" + playerMoney + ".");
                    } else {
                        System.out.println("It's a tie. Total funds now at $" + playerMoney + ".");
                    }
                    break;
                } else {
                        System.out.println("Invalid choice. Please enter 'h' or 's' or 'd'.");
                    }
                }
                if (playerMoney < 25) {
                    System.out.print("Insufficient funds. Game over.");
                    break;
                } else {
                    System.out.println("Do you want to play again? (y/n)");
                    String playAgain = scanner.nextLine();

                    if (playAgain.equalsIgnoreCase("n")) {
                        break;
                    } else if (playAgain.equalsIgnoreCase("c")) {
                        System.out.println("What is the count?");
                        int guessCount = sc.nextInt();
                        if (guessCount == count) {
                            System.out.println("Correct! Would you like to play again? (y/n)");
                            String playAgainAgain = scanner.nextLine();
                            if (playAgainAgain.equalsIgnoreCase("n")) {
                                break;
                            }
                            System.out.println(playAgainAgain);
                        } else {
                            System.out.println("Incorrect. Count is " + count + ". Game Over");
                            break;
                        }
                    }
                }
            }
            scanner.close();
        }
    }