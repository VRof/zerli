package clientClasses;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 *  Item in catalog data class
 *
 * <p> Project Name: Zer-Li (Java Application Flower Store) </p>
 *
 * @author Habib Ibrahim, Vitaly Rofman, Ibrahim Daoud, Yosif Hosen
 * @version  V1.00  2022
 */

public class ItemInCatalog {
    private VBox itemInCatalogVBox;
    private int id;
    private String isBundle;
    private String itemColor;
    private double itemPrice;

    public ItemInCatalog(int id, ImageView itemImage, String itemName, double itemPrice,String itemColor, String isBundle) {
        this.id = id;
        this.isBundle = isBundle;
        itemPrice = itemPrice*100;
        itemPrice = Math.round(itemPrice);
        itemPrice = itemPrice /100;
        this.itemPrice = itemPrice;
        this.itemColor = itemColor;
        Text lbl_itemName = new Text(itemName);
        lbl_itemName.setStyle("-fx-font-size: 15");
        Text lbl_itemPrice = new Text(itemPrice + " ₪");
        lbl_itemPrice.setStyle("-fx-font-size: 15");
        itemInCatalogVBox = new VBox(5);
        itemInCatalogVBox.getChildren().addAll(itemImage, lbl_itemName, lbl_itemPrice);
        itemInCatalogVBox.setSpacing(10);
        itemInCatalogVBox.setAlignment(Pos.CENTER);
        itemInCatalogVBox.setStyle("-fx-background-color:  #b6f2e0");
    }

    public String getItemColor() {
        return itemColor;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getId() {
        return id;
    }

    public boolean isBundle() {
        return isBundle.equals("TRUE");
    }

    /**
     *
     * @return VBox representation of item
     */
    public VBox getItemInCatalogVBox() {
        return itemInCatalogVBox;
    }
}
