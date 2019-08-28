package com.taobao.tmalljpa.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

public class NavigatorPage<T> {
    private long totalElements;
    private int totalPages;
    private List<T> content;
    private int number;
    private int numberOfElements;
    private Pageable pageable;
    private int size;
    private Sort sort;
    private boolean hasContent;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isEmpty;
    private boolean isFirst;
    private boolean isLast;
    private Pageable nextPageable;
    private Pageable previousPageable;
    private int pageNumbs;
    private int[] pageElements;
    public NavigatorPage(){
        //这个空的分页是为了 Redis 从 json格式转换为 Page4Navigator 对象而专门提供的
    }


    public NavigatorPage(Page<T> page , int pageNumbs) {
        this.pageNumbs = pageNumbs;
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.content = page.getContent();
        this.number = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.pageable = page.getPageable();
        this.size = page.getSize();
        this.sort = page.getSort();
        this.hasContent = page.hasContent();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.isEmpty = page.isEmpty();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.nextPageable = page.nextPageable();
        this.previousPageable = page.previousPageable();
        //调用计算页码
        this.calculatePageElements();
    }

    //计算页码数组
    public void calculatePageElements(){
        if(this.pageNumbs >= 1 && this.pageNumbs%2 == 1){//显示数量需合法
            /*  存在两种可能 */
            //当页码数大于等于总页数时
            if(this.pageNumbs >= this.totalPages){
                this.pageElements = new int[this.totalPages];
                for(int i =0 ;i<this.totalPages;i++){
                    this.pageElements[i] = i;
                }
            }
            //当页码数小于总页数时
            if(this.pageNumbs < this.totalPages){
                this.pageElements = new int[this.pageNumbs];
                int offSize = this.pageNumbs/2;
                ToolClass.out("offSize="+offSize);
                int inferCount = 0;
                //当前页码
                this.pageElements[inferCount] = this.number;
                inferCount ++;
                //记录最小页码
                int minPage = 0;
                //左侧页码
                for(int left = this.number;left > 0;left--){
                    this.pageElements[inferCount] = minPage = left - 1;
                    inferCount ++;
                    if(inferCount > offSize){
                        break;
                    }
                }
                //右侧页码
                int addPageSize = offSize - (inferCount - 1);//left 缺页的向right 补齐
                offSize = this.number + (offSize + addPageSize);
                for(int right = this.number;right < offSize && right < this.totalPages -1; right++){
                    this.pageElements[inferCount] = right + 1;
                    inferCount ++;
                }
                //右侧页码不足，再向左边补齐
                if(inferCount < this.pageNumbs){
                    for(int left = this.pageNumbs - inferCount;left > 0;left--){
                        this.pageElements[inferCount] = left;
                        inferCount ++;
                    }
                }
            }
            //排序
            Arrays.sort(this.pageElements);
            ToolClass.out("pages="+ Arrays.toString(this.pageElements));
        }else {
            try {
                throw new Exception("分页导航--->显示页数需要合法：大于3，且不为偶数!");
            }catch (Exception e){
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public boolean isHasContent() {
        return hasContent;
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public Pageable getNextPageable() {
        return nextPageable;
    }

    public void setNextPageable(Pageable nextPageable) {
        this.nextPageable = nextPageable;
    }

    public Pageable getPreviousPageable() {
        return previousPageable;
    }

    public void setPreviousPageable(Pageable previousPageable) {
        this.previousPageable = previousPageable;
    }

    public int getPageNumbs() {
        return pageNumbs;
    }

    public void setPageNumbs(int pageNumbs) {
        this.pageNumbs = pageNumbs;
    }

    public int[] getPageElements() {
        return pageElements;
    }

    public void setPageElements(int[] pageElements) {
        this.pageElements = pageElements;
    }

    @Override
    public String toString() {
        return "NavigatorPage{" +
                "totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", content=" + content +
                ", number=" + number +
                ", numberOfElements=" + numberOfElements +
                ", pageable=" + pageable +
                ", size=" + size +
                ", sort=" + sort +
                ", hasContent=" + hasContent +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious +
                ", isEmpty=" + isEmpty +
                ", isFirst=" + isFirst +
                ", isLast=" + isLast +
                ", nextPageable=" + nextPageable +
                ", previousPageable=" + previousPageable +
                ", pageNumbs=" + pageNumbs +
                ", pageElements=" + Arrays.toString(pageElements) +
                '}';
    }
}
