package com.epam.spring.parsers;

import com.epam.spring.entity.Product;
import com.epam.spring.entity.ProductCategory;
import com.epam.spring.entity.User;
import com.epam.spring.exception.ExceptionFactory;
import com.epam.spring.repository.ProductRepository;
import com.epam.spring.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParseProductToDB {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseProductToDB.class);
    private ApplicationContext context = ApplicationContextProvider.getApplicationContext();
    private ProductRepository productRepository;
    private final String SEPARATOR = ";";


    public ParseProductToDB(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //            Integer.valueOf(productArrays[0]),    idCategory
//            productArrays[1].trim(),              alias
//            productArrays[2].trim(),              title
//            productArrays[3].trim(),              text_short
//            productArrays[4].trim(),              textFull
//            Double.valueOf(productArrays[5]),     price
//            Integer.valueOf(productArrays[6]),    qty
//      category;alias;title;text_short;text_full;price;qty
//      # - comment
//      ; - SEPARATOR
    public void parseFile(String file, User user) {
        Map<String, Integer> mapIdCategory = getMapIdCategory();

        try (BufferedReader bufferedReader = getBufferedReader(file)) {
            String[] productArrays;
            while (bufferedReader.ready()) {
                productArrays = bufferedReader.readLine().split(SEPARATOR);
                if (productArrays[0].startsWith("#")) continue;
                if (productArrays.length != 7) {
                    LOGGER.info("File format incorrect" + Arrays.toString(productArrays));
                    continue;
                }
                if (mapIdCategory.containsKey(productArrays[0].trim())) {
                    Product product = productRepository.addProduct(mapIdCategory.get(productArrays[0].trim()), productArrays[1].trim(),
                            productArrays[2].trim(), productArrays[3].trim(), productArrays[4].trim(),
                            Double.valueOf(productArrays[5].trim()), Integer.valueOf(productArrays[6].trim()), user);
                    LOGGER.info("Add new product, id: " + product.getId() + " ,title: " + product.getTitle() +
                            ", price:" + product.getPrice());
                } else {
                    ProductCategory productCategory = productRepository.addProductCategory(8, "", productArrays[0].trim(),
                            productArrays[0].trim(), user);
                    LOGGER.info("Add new product category id: " + productCategory.getId() + ", title:" + productCategory.getTitle());

                    Product product = productRepository.addProduct(productCategory.getId(), productArrays[1].trim(),
                            productArrays[2].trim(), productArrays[3].trim(), productArrays[4].trim(),
                            Double.valueOf(productArrays[5].trim()), Integer.valueOf(productArrays[6].trim()), user);

                    LOGGER.info("Add new product, id: " + product.getId() + " ,title: " + product.getTitle() +
                            ", price:" + product.getPrice());
                }
            }
        } catch (IOException e) {
            LOGGER.error("File error, " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid file format, " + e.getMessage(), e);
        }
    }

    private Map<String, Integer> getMapIdCategory() {
        List<ProductCategory> productCategories = productRepository.getListProductCategory();
        Map<String, Integer> map = context.getBean(HashMap.class);
        for (ProductCategory pc : productCategories) {
            map.put(pc.getTitle(), pc.getId());
        }
        return map;
    }

    private BufferedReader getBufferedReader(String file) {
        try {
            Constructor<BufferedReader> bufferedReaderConstructor = BufferedReader.class.getConstructor(Reader.class);
            Constructor<FileReader> fileReaderConstructor = FileReader.class.getConstructor(String.class);
            return bufferedReaderConstructor.newInstance(fileReaderConstructor.newInstance(file));
        } catch (Exception e) {
            throw ExceptionFactory.getIllegalArgumentException("Error create bufferedReader");
        }
    }
}
