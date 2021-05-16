package com.fool.community.dto;

import java.util.ArrayList;
import java.util.List;

public class PageDTO<T> {
    //内容容器
    private List<T> data;
    //是否展示前一页
    private boolean showPre;
    //是否展示后一页
    private boolean showNext;
    //是否展示第一页
    private boolean showFirst;
    //是否展示最后一页
    private boolean showLast;
    //当前页码
    private int page;
    //当前展示的页码集合
    private List<Integer> pages = new ArrayList<>();
    //所有页数
    private int totalPage;

    public void setPagination(int totalcount, int page, int size) {
        //如果取余等于0，说明文章数恰好是size的倍数，页数是整数
        //如果不等于0，说明非整数倍，例如6%5=1,,此时的页数应该是6/5+1=1+1=2，1页装不下必须额外加1页
        if (totalcount % size == 0) {
            totalPage = totalcount / size;
        } else {
            totalPage = totalcount / size + 1;
        }

        //page<1就显示1，page>最大页数就显示最大页数
        //例如当输入page=-1，逻辑上不应该有-1页，因此直接将page改为1
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        this.page = page;
        //将需要展示的页码插入到pages中
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }
        //是否展示上一页
        if (page == 1) {
            showPre = false;
        } else {
            showPre = true;
        }
        //是否展示下一页
        //如果页码等于总页码，说明已经到了最后1页，不用再展示下一页按钮，否则展示
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }
        //是否展示第一页
        //如果页码里有第1页，就不用展示第一页按钮，反之显示
        if (pages.contains(1)) {
            showFirst = false;
        } else {
            showFirst = true;
        }
        //是否展示最后一页
        //如果页码包含总页码，说明已经到最后一页，不用展示最后一页按钮
        if (pages.contains(totalPage)) {
            showLast = false;
        } else {
            showLast = true;
        }
    }

    //getter and setter方法
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isShowPre() {
        return showPre;
    }

    public void setShowPre(boolean showPre) {
        this.showPre = showPre;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public boolean isShowFirst() {
        return showFirst;
    }

    public void setShowFirst(boolean showFirst) {
        this.showFirst = showFirst;
    }

    public boolean isShowLast() {
        return showLast;
    }

    public void setShowLast(boolean showLast) {
        this.showLast = showLast;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalpage(int totalPage) {
        this.totalPage = totalPage;
    }
}