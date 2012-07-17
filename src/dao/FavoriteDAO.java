package dao;

import bean.FavoriteNote;

import java.util.List;


public interface FavoriteDAO {
    List<FavoriteNote> selectAllItem();

    void deleteItem(Integer no);

    void insertItem(int noteNo);

    boolean isExist(int noteNo);
}
