package com.example.ordersystem.model;

public class Pagination {

    // Number of items in a page
    private int pageSize = 12;
    private int blockSize = 12;
    // Current page
    private int page = 1;
    // Current block
    private int block = 1;
    // Number of items
    private int totalItems;
    // Number of pages
    private int totalPages;
    // Number of blocks
    private int totalBlocks;


    private int startPage = 1;
    private int lastPage = 1;

    // Index of first item used from DB
    private int beginIndex = 0;

    private int prevBlock;
    private int nextBlock;


    public Pagination(int totalItems, int page) {


        setPage(page);

        setTotalItems(totalItems);

        setTotalPages((int) Math.ceil(totalItems * 1.0 / pageSize));

        setTotalBlocks((int) Math.ceil(totalPages * 1.0 / blockSize));

        setBlock((int) Math.ceil((page * 1.0)/blockSize));

        setStartPage((block - 1) * blockSize + 1);

        setLastPage(startPage + blockSize - 1);

        if(lastPage > totalPages){this.lastPage = totalPages;}

        setPrevBlock((block * blockSize) - blockSize);

        if(prevBlock < 1) {this.prevBlock = 1;}

        setNextBlock((block * blockSize) + 1);

        if(nextBlock > totalPages) {nextBlock = totalPages;}

        setBeginIndex((page-1) * pageSize);

    }




    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalBlocks() {
        return totalBlocks;
    }

    public void setTotalBlocks(int totalBlocks) {
        this.totalBlocks = totalBlocks;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getPrevBlock() {
        return prevBlock;
    }

    public void setPrevBlock(int prevBlock) {
        this.prevBlock = prevBlock;
    }

    public int getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(int nextBlock) {
        this.nextBlock = nextBlock;
    }
}
