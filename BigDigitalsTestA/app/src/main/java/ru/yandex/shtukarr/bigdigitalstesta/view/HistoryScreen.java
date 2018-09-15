package ru.yandex.shtukarr.bigdigitalstesta.view;


import ru.yandex.shtukarr.bigdigitalstesta.LinkModel;

public interface HistoryScreen {
    void onItemSelected(LinkModel linkModel);
    void refresh();
}
