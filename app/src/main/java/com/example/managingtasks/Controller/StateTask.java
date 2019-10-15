package com.example.managingtasks.Controller;


public enum StateTask {
    TODO, DOING, DONE;

    public static StateTask toStateTask(int i) {
        StateTask stateTask =null;
        switch (i) {
            case 0: {
                stateTask =StateTask.TODO;
                break;
            }
            case 1: {
                stateTask =StateTask.DOING;
                break;
            }
            case 2: {
                stateTask =StateTask.DONE;
                break;
            }
        }
        return stateTask;
    }

    public int getValue(StateTask stateTask){
        if (stateTask == StateTask.TODO)
            return 0;
        if (stateTask == StateTask.DOING)
            return 1;
        if (stateTask == StateTask.DONE)
            return 2;
        return -1;
    }
}

