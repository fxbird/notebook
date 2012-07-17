package dao.hbn;

import bean.FavoriteNote;
import dao.FavoriteDAO;

import java.util.List;

public class FavoriteDAOHbn implements FavoriteDAO {
    @Override
    public List<FavoriteNote> selectAllItem() {
        return null;
    }

    @Override
    public void deleteItem(Integer no) {

    }

    @Override
    public void insertItem(int noteNo) {

    }

    @Override
    public boolean isExist(int noteNo) {
        return false;
    }
}
