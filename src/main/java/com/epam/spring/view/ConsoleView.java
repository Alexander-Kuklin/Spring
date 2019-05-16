package com.epam.spring.view;


import com.epam.spring.entity.*;
import com.epam.spring.parsers.ParseProductToDB;
import com.epam.spring.repository.OrderRepository;
import com.epam.spring.repository.ProductRepository;
import com.epam.spring.repository.impl.ProductRepositoryImpl;
import com.epam.spring.repository.impl.UserRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.FailedLoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class ConsoleView {
    private final Logger LOGGER = LoggerFactory.getLogger(ConsoleView.class);

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private ParseProductToDB parseProductDB;
    private BufferedReader reader;

    private UserRepositoryImpl userRepository;
    private boolean isRunning = true;
    private User loginUser;

    public void run() {

        try {
            while (isRunning) {
                getMainMenu();
                switch (Integer.valueOf(reader.readLine())) {
                    case 1:
                        try {
                            loginUser = loginUser();
                        } catch (FailedLoginException e) {
                            System.out.println("Неверное имя пользователя или пароль.");
                        }
                        break;
                    case 2:
                        addProductInCart();
                        break;
                    case 3:
                        modifyProductQtyInCart();
                        break;
                    case 4:
                        deleteProductFromCart();
                        break;
                    case 5:
                        showCartUser();
                        break;
                    case 6:
                        showOrdersUser();
                        break;
                    case 7:
                        confirmOrderCart();
                        break;
                    case 8:
                        createCoupon();
                        break;
                    case 9:
                        parseFileCatalog();
                        break;
                    case 0:
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Неверная комманда\n");
                }
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.info("Ошибка чтения с консоли, " + e.getMessage(), e);
        }

    }

    private void parseFileCatalog() throws IOException {
        if (loginUser == null) {
            System.out.println("Необходимо залогиниться\n");
            return;
        }

        System.out.println("Формат файла: id_category;alias;title;text_short;text_full;price;qty");
        System.out.println("Введите название путь до файла");

        String source = reader.readLine().trim();

        if (Files.notExists(Paths.get(source))) {
            System.out.println("Файл не найден\n");
            return;
        }

        parseProductDB.parseFile(source, loginUser);
    }

    private void createCoupon() throws IOException {
        if (loginUser == null) {
            System.out.println("Необходимо залогиниться.");
            return;
        }

        System.out.println("Введите название купона:");
        String nameCoupon = reader.readLine().trim();
        System.out.println("Введите % скидки:");

        int discount = Integer.parseInt(reader.readLine().trim());

        System.out.println("Укажите категорию продуктов, на которую действует скидка, \n" +
                "0 - все товары, 1-7 группы:");

        int idProductCategory = Integer.parseInt(reader.readLine().trim());

        orderRepository.addCoupon(idProductCategory, nameCoupon, true, discount, 0,
                LocalDate.of(2019, 01, 01),
                LocalDate.of(2019, 12, 31),
                loginUser);
    }

    private void addProductInCart() {
        try {
            List<Product> productList = productRepository.getListProduct();
            for (Product product : productList) {
                System.out.println(product.getId() + " " + product.getTitle() + " " + product.getPrice());
            }

            System.out.println("Введите номер товара, 0 выход: ");
            int idProduct = Integer.parseInt(reader.readLine());
            if (idProduct == 0) return;
            System.out.println("Введите кол-во товара: ");
            int qtyProduct = Integer.parseInt(reader.readLine());
            //TODO: Сделать проверку на наличие товара.
            orderRepository.addProductQtyInCart(idProduct, qtyProduct, loginUser);
        } catch (IOException e) {
            LOGGER.error("Error read from console: " + e.getMessage(), e);
        }
    }


    private void modifyProductQtyInCart() {
        try {
            showCartUser();
            System.out.println("Введите номер товара для изменения, 0 выход: ");
            int idProduct = Integer.parseInt(reader.readLine());
            if (idProduct == 0) return;
            System.out.println("Введите новое кол-во товара: ");
            int qtyProduct = Integer.parseInt(reader.readLine());
            orderRepository.modifyProductQtyInCart(idProduct, qtyProduct, loginUser);
        } catch (IOException e) {
            LOGGER.error("Error read from console: " + e.getMessage(), e);
        }
    }

    private void deleteProductFromCart() {
        try {
            showCartUser();
            System.out.println("Введите номер товара для удаления, 0 выход: ");
            int idProduct = Integer.parseInt(reader.readLine());
            if (idProduct == 0) return;
            orderRepository.deleteProductFromCart(idProduct, loginUser);
        } catch (IOException e) {
            LOGGER.error("Error read from console: " + e.getMessage(), e);
        }
    }

    private void confirmOrderCart() {
        //TODO: Добавить подтверждение заказа с указанием типа оплаты
//        Order cart = orderService.getListOrderUserWithStatus(loginUser, OrderStatus.CART).get(0);
//        List<OrderItem> orderItemsList = orderService.getListOrderItem(cart.getId());
//
//        if (orderItemsList.size() != 0) {
//            orderService.confirmOrder(loginUser, cart.getId());
//            System.out.println("Заказ сформирован и отправлен на проверку.");
//        } else {
//            System.out.println("Корзина пуста, добавьте товары.\n");
//        }

    }

    private void showCartUser() {
        if (loginUser == null) {
            System.out.println("Необходимо залогиниться.");
        } else {
            Order userCart = orderRepository.getUserCart(loginUser);
            System.out.println("Товаров в корзине на: " + userCart.getPrice() + " рублей");
            List<OrderItem> orderItemsList = orderRepository.getListOrderItem(userCart);
            for (OrderItem orderItem : orderItemsList) {
                Product product = productRepository.getProductById(orderItem.getIdProduct());
                System.out.println("id:" + product.getId() + " Товар:" + product.getTitle() + " кол-во:" + orderItem.getQty()
                        + " цена:" + orderItem.getPrice());
            }
        }
        System.out.println();
    }

    private void showOrdersUser() {
        if (loginUser == null) {
            System.out.println("Необходимо залогиниться.");
        } else {
            List<Order> orderList = orderRepository.getListOrderUser(loginUser);
            if (orderList.size() == 0) {
                System.out.println("*** У пользователя нет заказов");
                return;
            }
            List<OrderItem> orderItemsList;
            for (Order order : orderList) {
                if (order.getOrderStatus() != OrderStatus.CART) {
                    System.out.println("Заказ № " + order.getId() + " сумма заказа:" + order.getPrice() +
                            " статус заказа:" + order.getOrderStatus());
                    orderItemsList = orderRepository.getListOrderItem(order);
                    for (OrderItem orderItem : orderItemsList) {
                        Product product = productRepository.getProductById(orderItem.getIdProduct());
                        System.out.println("id:" + product.getId() + " Товар:" + product.getTitle() + " кол-во:" + orderItem.getQty()
                                + " цена:" + orderItem.getPrice());
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    private User loginUser() throws FailedLoginException, IOException {
        System.out.println("логин:ivan@example.com - пароль:12345");
        System.out.println("Введите имя пользователя: ");
        String login = reader.readLine().trim();
        System.out.println("Введите пароль: ");
        String password = reader.readLine();
        return userRepository.validateUserPassword(login, password);
    }

    private void getMainMenu() {
        String user = "";
        if (loginUser != null)
            user = loginUser.getName();

        System.out.println("\tМеню:\t\t\t\tПользователь:" + user + "\n" +
                "1. Логин пользователя.\n" +
                "2. Добавить товар в корзину\n" +
                "3. Изменить кол-во товара в корзине\n" +
                "4. Удалить товар из корзины\n" +
                "5. Просмотр корзины.\n" +
                "6. Просмотр всех заказов.\n" +
                "7. Оформить заказ.\n" +
                "8. Создать промокод.\n" +
                "9. Загрузить товары из файла.\n" +
                "0. Выход.\n\n" +
                "Введите комманду:");
    }

    public void setProductRepositoryImpl(ProductRepositoryImpl productRepositoryImpl) {
        this.productRepository = productRepositoryImpl;
    }

    public void setParseProductToDB(ParseProductToDB parseProductToDB) {
        this.parseProductDB = parseProductToDB;
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void setUserRepositoryImpl(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.reader = bufferedReader;
    }
}
