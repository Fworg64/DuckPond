package com.fworg64.duckpond.game;

import java.util.List;

/**
 * Created by fworg on 6/7/2016.
 */
public interface Browsable {

    public List<String> getAllOptions(); //returns all current options

    public void pageInto(String option); //pages into the option given and changes the output of getAllOptions to reflect the change
                                         //if the item paged into was the final selection, isFinalSelection should then return true
                                         //also in this case, getAllOptions should be unchanged

    public void pageUp(); //goes up on level in the browsable tree

    public boolean canPageUp(); //true if the current level is not the top of the browsable item

    public boolean isFinalSelection(); //true if the item picked is an item and not a folder or something

    public String getSelectionName(); //returns the name of the selected item, only needs to be valid if isFinalSelection is true

    public String getSelectionContents(); //returns the contents of the selected item, only needs to be valid if isFinalSelection is true

    public void close(); //close the underliying resource


}
