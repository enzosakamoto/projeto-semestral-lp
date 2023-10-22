package pages;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import shared.entities.Client;
import shared.entities.Meal;
import shared.entities.Order;
import shared.infra.repository.Repository;

public class Checkout extends JFrame implements ActionListener {
    private JLabel checkout_title;
    private JLabel credits;
    private JLabel meal;
    private JLabel price;
    private JLabel credits_remain;
    private JLabel credits_remain_price;
    private JButton confirm;
    private JButton cancel;
    private double price_value;
    private String meal_text;
    private Client client = Home.client;
    Repository repository = new Repository(Home.bn);
    RestaurantPage restaurantPage;

    public Checkout(Meal meal, RestaurantPage restaurantPage) {
        super("Checkout");
        this.price_value = meal.getPrice();
        this.meal_text = meal.getName();
        this.restaurantPage = restaurantPage;
        setSize(750, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Font font = new Font("Arial", Font.PLAIN, 22);

        Container container = getContentPane();
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        container.add(panel);

        checkout_title = new JLabel("Checkout");
        checkout_title.setFont(font);
        credits = new JLabel("Créditos: " + client.getCredits());
        credits.setFont(font);
        this.meal = new JLabel("Comida: " + meal_text);
        this.meal.setFont(font);
        this.price = new JLabel("R$ " + price_value);
        this.price.setFont(font);
        credits_remain = new JLabel("Créditos restantes: ");
        credits_remain.setFont(font);
        credits_remain_price = new JLabel("R$ " + (client.getCredits() - price_value));
        credits_remain_price.setFont(font);

        confirm = new JButton("Confirmar");
        confirm.setFont(font);
        cancel = new JButton("Cancelar");
        cancel.setFont(font);

        panel.add(checkout_title);
        panel.add(credits);
        panel.add(this.meal);
        panel.add(this.price);
        panel.add(credits_remain);
        panel.add(credits_remain_price);
        panel.add(confirm);
        panel.add(cancel);

        confirm.addActionListener(this);
        cancel.addActionListener(this);

        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirm) {
            if (this.client.getCredits() - this.price_value < 0) {
                JOptionPane.showMessageDialog(null, "Você não tem créditos suficientes para realizar essa compra.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    repository.updateClientCreditsByIdClient(this.client.getId(),
                            this.client.getCredits() - this.price_value);
                    repository
                            .createOrder(
                                    new Order(restaurantPage.restaurant.getId(), this.client.getId(), this.meal_text));
                    JOptionPane.showMessageDialog(null, "Compra realizada com sucesso!", "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    this.restaurantPage.dispose();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar compra.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == cancel) {
            this.dispose();
        }
    }

}