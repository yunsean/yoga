package com.yoga.core.data;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PageList<T> extends ArrayList<T> {
    private static final long serialVersionUID = 3619077579435219753L;

    private CommonPage page;

    public PageList() {
        page = new CommonPage();
    }

    public PageList(Collection<T> c) {
        this(c, null);
    }

    public PageList(Collection<T> c, CommonPage page) {
        super(c);
        this.page = (page == null) ? new CommonPage() : page;
    }

    public PageList(CommonPage page) {
        super();
        this.page = (page == null) ? new CommonPage() : page;
    }

    public PageList(Page<T> p) {
        super();
        if (null != p && p.getSize() > 0) {
            Iterator<T> ite = p.iterator();
            while (ite.hasNext()) {
                this.add(ite.next());
            }
            this.page = new CommonPage(p.getNumber(), p.getSize(), p.getTotalElements());
        }
    }

    public PageList(List<T> p) {
        super();
        if (null != p && p.size() > 0) {
            Iterator<T> ite = p.iterator();
            while (ite.hasNext()) {
                this.add(ite.next());
            }
            this.page = new CommonPage(0, p.size(), p.size());
        }
    }

    public PageList(List<T> p, int pageIndex, int pageSize, int totalCount) {
        super();
        if (null != p && p.size() > 0) {
            Iterator<T> ite = p.iterator();
            while (ite.hasNext()) {
                this.add(ite.next());
            }
        }
        this.page = new CommonPage(pageIndex, pageSize, totalCount);
    }
    public PageList(int pageIndex, int pageSize, int totalCount) {
        super();
        this.page = new CommonPage(pageIndex, pageSize, totalCount);
    }

    public CommonPage getPage() {
        if (page == null) return new CommonPage();
        else return page;
    }

    public void setPage(CommonPage page) {
        this.page = page;
    }
}
