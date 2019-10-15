package com.example.managingtasks.GreenDao;

import com.example.managingtasks.Controller.StateTask;

import org.greenrobot.greendao.converter.PropertyConverter;

public class StateConverter implements PropertyConverter<StateTask,Integer> {
    @Override
    public StateTask convertToEntityProperty(Integer databaseValue) {

        switch (databaseValue) {
            case 0: {
                return StateTask.TODO;
            }
            case 1: {
                return StateTask.DOING;
            }
            case 2: {
                return StateTask.DONE;
            }

        }
        return null;
    }

    @Override
    public Integer convertToDatabaseValue(StateTask entityProperty) {
        if (entityProperty == StateTask.TODO)
            return 0;
        if (entityProperty == StateTask.DOING)
            return 1;
        if (entityProperty == StateTask.DONE)
            return 2;
        return null;
    }
}
