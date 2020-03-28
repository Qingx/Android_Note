package com.hua.note.config;

import com.google.gson.Gson;
import com.hua.note.data.NoteEntity;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Iaovy on 2020/3/27 16:28
 *
 * @email Cymbidium@outlook.com
 */
public class NoteConverter implements PropertyConverter<ArrayList<NoteEntity>, String> {
    @Override
    public ArrayList<NoteEntity> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        List<String> list_str = Arrays.asList();
        ArrayList<NoteEntity> list_transport = new ArrayList<>();
        for (String s : list_str) {
            list_transport.add(new Gson().fromJson(s, NoteEntity.class));
        }
        return list_transport;
    }

    @Override
    public String convertToDatabaseValue(ArrayList<NoteEntity> entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (NoteEntity array : entityProperty) {
                String str = new Gson().toJson(array);
                sb.append(str);
                sb.append(",");
            }
            return sb.toString();
        }
    }
}
