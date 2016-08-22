package org.pasr.gui.email.tree;


import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.pasr.prep.email.fetchers.Folder;

import java.util.ArrayList;
import java.util.Arrays;


public class EmailTree extends TreeView<String> {
    public void add(Folder folder){
        EmailTreeItem treeItem = new EmailTreeItem(folder);

        String[] folders = folder.getPath().split("/");
        int numberOfFolders = folders.length;

        int depth = 0;
        EmailTreeItem currentFolder = (EmailTreeItem) getRoot();
        while(depth < numberOfFolders){
            EmailTreeItem existingSubFolder = containsAsFolder(currentFolder, folders[depth]);
            if (existingSubFolder != null){
                currentFolder = existingSubFolder;
                depth++;

                if(depth == numberOfFolders - 1){
                    currentFolder.getChildren().add(treeItem);
                    break;
                }
            }
            else{
                break;
            }
        }
        if(depth < numberOfFolders - 1 || depth == 0){
            for(int i = depth, n = numberOfFolders - 1;i < n;i++){
                EmailTreeItem parentFolder = new EmailTreeItem(new Folder(
                    String.join("/", (CharSequence[]) Arrays.copyOfRange(folders, 0, i + 1)),
                    new ArrayList<>()
                ));

                currentFolder.getChildren().add(parentFolder);

                currentFolder = parentFolder;
            }

            currentFolder.getChildren().add(treeItem);
        }
    }

    private EmailTreeItem containsAsFolder(EmailTreeItem item, String value){
        for(TreeItem<String> child : item.getChildren()){
            if(child.getValue().equals(value) &&
                ((EmailTreeItem)(child)).isFolder()){
                return (EmailTreeItem) child;
            }
        }

        return null;
    }

}
