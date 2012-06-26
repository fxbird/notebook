package test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageListDnDTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new ImageListDnDFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

class ImageListDnDFrame extends JFrame {
    private ImageList list1;
    private ImageList list2;

    ImageListDnDFrame() throws HeadlessException {
        setTitle("ImageListDnDTest");
        setSize(600, 500);

        list1 = new ImageList(new File("h:\\My Documents\\program\\java\\projects\\idea\\book2\\src\\test\\images1\\").listFiles());
        list2 = new ImageList(new File("h:\\My Documents\\program\\java\\projects\\idea\\book2\\src\\test\\images2\\").listFiles());
        setLayout(new GridLayout(2, 1));
        add(new JScrollPane(list1));
        add(new JScrollPane(list2));
    }
}

class ImageList extends JList {
    ImageList(File[] imageFiles) {
        {
            DefaultListModel model = new DefaultListModel();
            for (File f : imageFiles) {
                model.addElement(new ImageIcon(f.getPath()));
            }

            setModel(model);
            setVisibleRowCount(0);
            setLayoutOrientation(JList.HORIZONTAL_WRAP);
            setDragEnabled(true);
            setDropMode(DropMode.ON_OR_INSERT);
            setTransferHandler(new ImageListTransferHandler());
        }
    }
}

class ImageListTransferHandler extends TransferHandler {
    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent source) {
        JList list = (JList) source;
        int index = list.getSelectedIndex();
        if (index < 0) return null;
        ImageIcon icon = (ImageIcon) list.getModel().getElementAt(index);
        return new ImageTransferable(icon.getImage());
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (action == MOVE) {
            JList list = (JList) source;
            int index = list.getSelectedIndex();
            if (index < 0) return;
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.remove(index);
        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            if (support.getUserDropAction() == MOVE) support.setDropAction(COPY);
            return true;
        } else {
            return support.isDataFlavorSupported(DataFlavor.imageFlavor);
        }
    }

    @Override
    public boolean importData(TransferSupport support) {
        JList list = (JList) support.getComponent();
        DefaultListModel model = (DefaultListModel) list.getModel();

        Transferable transferable = support.getTransferable();
        List<DataFlavor> flavors = Arrays.asList(transferable.getTransferDataFlavors());

        List<Image> images = new ArrayList<Image>();

        try {
            if (flavors.contains(DataFlavor.javaFileListFlavor)) {
                List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                for (File f : fileList) {
                    try {
                        images.add(ImageIO.read(f));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            } else if (flavors.contains(DataFlavor.imageFlavor)) {
                images.add((Image) transferable.getTransferData(DataFlavor.imageFlavor));
            }

            int index;

            if (support.isDrop()) {
                JList.DropLocation location = (JList.DropLocation) support.getDropLocation();
                index = location.getIndex();
                if (!location.isInsert()) model.remove(index);
            } else {
                index = model.size();
            }
            

            for (Image image : images) {
                model.add(index, new ImageIcon(image));
                index++;
            }

            return true;
        } catch (Exception ex) {
            return false;
        }

    }
}
