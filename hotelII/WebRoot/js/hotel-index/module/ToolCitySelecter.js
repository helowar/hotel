var MGTOOL = MGTOOL || {}; (function() {
    function a() {
        this.fns = {}
    }
    a.prototype = {
        constructor: a,
        addFns: function(g, f) {
            if (typeof this.fns[g] == "undefined") {
                this.fns[g] = []
            }
            this.fns[g].push(f)
        },
        fire: function(k) {
            if (!k.target) {
                k.target = this
            }
            if (this.fns[k.type] instanceof Array) {
                var h = this.fns[k.type];
                for (var g = 0,
                f = h.length; g < f; g++) {
                    h[g](k)
                }
            }
        },
        removefn: function(k, h) {
            if (this.fns[k] instanceof Array) {
                var h = fns[k];
                for (var g = 0,
                f = fns.length; g < f; g++) {
                    if (fns[g] === h) {
                        break
                    }
                }
                fns.splice(g, 1)
            }
        }
    };
    function e(g, f) {
        this.target = g;
        this.boxid = f
    }
    e.prototype = {
        createBox: function() {
            if (!MGTOOL.$id(this.boxid)) {
                var f = document.createElement("div");
                f.id = this.boxid;
                document.body.appendChild(f)
            }
            this.wrap = MGTOOL.$id(this.boxid);
            return this.wrap
        },
        setPos: function() {
            var l = MGTOOL.getoffset(this.target),
            n = l.top,
            o = l.left,
            q = window.pageYOffset || document.documentElement ? document.documentElement.scrollTop: document.body.scrollTop,
            i = window.pageXOffset || document.documentElement ? document.documentElement.scrollLeft: document.body.scrollLeft,
            g = this.target.offsetHeight,
            k = this.target.offsetWidth,
            p = this.wrap.offsetHeight,
            h = this.wrap.offsetWidth,
            m = document.documentElement.clientHeight,
            f = document.documentElement.clientWidth;
            this.wrap.style.display = "block";
            if (n - q + p > m && n - q > p) {
                y = n - p;
                this.wrap.style.top = y + "px"
            } else {
                y = n + g;
                this.wrap.style.top = y + "px"
            }
            if (o - i + h > f && o - i > h) {
                x = o - h + k;
                this.wrap.style.left = x + "px"
            } else {
                x = o;
                this.wrap.style.left = x + "px"
            }
        },
        resetPost: function() {
            this.wrap.style.display = "none"
        }
    };
    function c(f) {
        this.boxID = f.boxID || "warnID"
    }
    c.prototype = {
        createBox: function() {
            if (!MGTOOL.$id(this.boxID)) {
                var f = document.createElement("div");
                f.id = this.boxID;
                f.style.display = "none";
                document.body.appendChild(f)
            }
            this.wrap = MGTOOL.$id(this.boxID)
        },
        setPos: function() {
            var l = MGTOOL.getoffset(this.target),
            n = l.top,
            o = l.left,
            q = window.pageYOffset || document.documentElement ? document.documentElement.scrollTop: document.body.scrollTop,
            i = window.pageXOffset || document.documentElement ? document.documentElement.scrollLeft: document.body.scrollLeft,
            g = this.target.offsetHeight,
            k = this.target.offsetWidth,
            p = this.wrap.offsetHeight,
            h = this.wrap.offsetWidth,
            m = document.documentElement.clientHeight,
            f = document.documentElement.clientWidth;
            this.wrap.style.display = "block";
            if (o - i + h > f && o - i > h) {
                x = o - h + k;
                this.wrap.style.left = x + "px";
                y = n - p - g;
                this.wrap.style.top = y + "px"
            } else {
                x = o + k;
                this.wrap.style.left = x + "px";
                y = n;
                this.wrap.style.top = y + "px"
            }
        },
        setWarn: function(g, f) {
            this.target = g;
            if (!this.target) {
                return false
            }
            this.warnText = f || "请重新选择";
            this.createBox();
            this.wrap.className = "warn";
            this.wrap.innerHTML = this.warnText;
            this.setPos()
        }
    };
    function d(f) {
        this.target = f.target;
        this.hideInput = f.hideInput || null;
        this.hotDatas = f.hotDatas || [];
        this.keywrapid = f.keywrapid || "mgKeyWrap";
        this.firstFocus = false;
        this.onFocus = new a();
        this.onClick = new a();
        this.hotPop = new e(this.target, this.keywrapid);
        this.wrap = this.hotPop.createBox();
        this.bindEvent()
    }
    d.prototype = {
        setValue: function(f) {
            f.target.value = f.node.firstChild.nodeValue;
            if (f.hideinput) {
                f.hideinput.value = f.node.href.split("#")[1]
            }
        },
        addList: function(k, n) {
            if (!k) {
                return false
            }
            var o = "",
            f = n.length;
            k.className = "cityWrap cpWrap";
            var g = '<p class="cptip">请选择出发地城市</p>',
            m = "";
            if (!f) {
                m = '<div id="hotPOP" class="hotpanel"><div class="hotlist">请确认列表数据是否存在</div></div>';
                k.innerHTML = g + m;
                return
            }
            for (var h = 0; h < f; h++) {
                o += '<a href="#' + n[h][2] + '">' + n[h][1] + "</a>"
            }
            m = '<div id="hotPOP" class="hotpanel"><div class="hotlist">' + o + "</div></div>";
            k.innerHTML = g + m;
            var l = this;
            MGTOOL.addEvent(MGTOOL.$id("hotPOP"), "mousedown",
            function(q) {
                MGTOOL.stopEvent(q);
                var i = MGTOOL.getTarget(q),
                p = i.nodeName;
                if (i && p === "A") {
                    l.onClick.fire({
                        type: "popClick",
                        target: l.target,
                        hideinput: l.hideInput,
                        node: i
                    });
                    l.firstFocus = false;
                    l.target.blur();
                    return
                }
            })
        },
        _focus: function() {
            if (this.firstFocus) {
                this.firstFocus = false;
                return
            }
            this.onFocus.fire({
                type: "onFirst",
                parentBox: this.wrap,
                list: this.hotDatas
            });
            if (this.hotPop) {
                this.hotPop.setPos()
            }
            var f = this;
            MGTOOL.addEvent(this.wrap, "mousedown",
            function(g) {
                f.firstFocus = true;
                return false
            })
        },
        _blur: function() {
            var f = this;
            if (this.firstFocus) {
                this.firstFocus = false;
                setTimeout(function() {
                    f.target.focus()
                },
                0);
                return
            }
            if (this.hotPop) {
                this.hotPop.resetPost()
            }
            this.wrap.onmousedown = null
        },
        bindEvent: function() {
            if (!this.target) {
                return false
            }
            var f = this;
            MGTOOL.addEvent(this.target, "focus",
            function() {
                f._focus()
            });
            MGTOOL.addEvent(this.target, "blur",
            function() {
                f._blur()
            })
        }
    };
    function b(f) {
        this.target = f.target;
        this.hideInput = f.hideInput || null;
        this.hotDatas = f.hotDatas || [];
        this.cityDatas = f.cityDatas || [];
        this.listNum = f.listNum || 10;
        this.wrapid = f.wrapid;
        this.keywrapid = f.keywrapid || "mgKeyWrap";
        this.group = f.group || ["ABCDEF", "GHIJKL", "MNOPQR", "STUVWXYZ"];
        this.inputDefaultValue = f.inputDefaultValue || "中文/拼音";
        this.firstFocus = false;
        this.keyhide = true;
        this.inputValue = null;
        this.curList = null;
        this.allList = null;
        this.page = 0;
        this.maxpage = 0;
        this.ON = null;
        this.navnum = 0;
        this.PP = null;
        this.panelnum = 0;
        this.onFocus = new a();
        this.onClick = new a();
        this.onKey = new a();
        this.hotPop = new e(this.target, this.wrapid);
        this.wrap = this.hotPop.createBox();
        this.keyPop = new e(this.target, this.keywrapid);
        this.keyWrap = this.keyPop.createBox();
        this._bindEvent()
    }
    b.prototype = {
        trim: function(f) {
            return f.replace(/^\s+|\s+$/g, "")
        },
        _getListFromLetter: function(h, k) {
            var l = "",
            g = 0,
            f = k.length;
            if (!k.length) {
                return l
            }
            for (g; g < f; g++) {
                if (this.trim(k[g][2]).charAt(0) === h) {
                    l += '<a href="#' + k[g][0] + '">' + k[g][1] + "</a>"
                }
            }
            return l
        },
        _getHotList: function(h) {
            var k = "",
            g = 0,
            f = h.length;
            if (!f) {
                return k
            }
            for (g; g < f; g++) {
                k += '<a href="#' + h[g][0] + '">' + h[g][1] + "</a>"
            }
            return k
        },
        _getPanelData: function(h, l) {
            var m = "<dl>",
            k = 0,
            g = h.split(""),
            f = g.length;
            if (!g.length) {
                return m += "</dl>"
            }
            for (k; k < f; k++) {
                m += this._getListFromLetter(g[k], l).length ? "<dt>" + g[k] + "</dt><dd>" + this._getListFromLetter(g[k], l) + "</dd>": ""
            }
            m += "</dl>";
            return m
        },
        _setValue: function(f, g) {
            f.value = g
        },
        setCommonValue: function(f) {
            this._setValue(f.target, f.node.firstChild.nodeValue);
            if (f.hideinput) {
                this._setValue(f.hideinput, f.node.href.split("#")[1])
            }
        },
        setKeyClick: function(f) {
            this._setValue(f.target, f.node.firstChild.firstChild.nodeValue);
            if (f.hideinput) {
                this._setValue(f.hideinput, f.node.href.split("#")[1])
            }
        },
        keyEnter: function(f) {
            this._setValue(f.target, f.node.firstChild.firstChild.nodeValue);
            if (f.hideinput) {
                this._setValue(f.hideinput, f.node.href.split("#")[1])
            }
        },
        _hotTab: function(k, h) {
            var m = k.getElementsByTagName("span"),
            f = MGTOOL.getbyClass(h, "div", "hotpanel"),
            g = 0,
            n = m.length;
            this.ON = (this.navnum !== 0) ? m[this.navnum] : m[0];
            MGTOOL.addClass(this.ON, "on");
            this.PP = (this.panelnum !== 0) ? f[this.panelnum] : f[0];
            MGTOOL.removeClass(this.PP, "hothidden");
            var l = this;
            for (; g < n; g++) { (function() {
                    var i = g;
                    MGTOOL.addEvent(m[i], "mousedown",
                    function(p) {
                        if (!MGTOOL.hasClass(m[i], "on")) {
                            var q = m[i].id.split("#")[1],
                            o = MGTOOL.$id(q);
                            MGTOOL.addClass(l.PP, "hothidden");
                            MGTOOL.removeClass(o, "hothidden");
                            l.PP = o;
                            MGTOOL.removeClass(l.ON, "on");
                            MGTOOL.addClass(m[i], "on");
                            l.ON = m[i];
                            l.navnum = i;
                            l.panelnum = i;
                            return false
                        }
                    })
                })()
            }
        },
        _getVacationData: function(m) {
            var g = m;
            var l = "",
            h = 0,
            f = g.length,
            k = "SZX";
            if (!f) {
                return l
            }
            if (MGTOOL.$id("startCity")) {
                k = MGTOOL.$id("startCity").value
            }
            for (; h < f; h++) {
                if (g[h][2] === "N") {
                    l += '<a class="noflag" href="#' + g[h][1] + '">' + g[h][0] + "</a>"
                } else {
                    if (g[h][2] === "Y") {
                        l += '<a href="http://lvyou.mangocity.com/line-' + k + "-" + g[h][1] + '">' + g[h][0] + "</a>"
                    } else {
                        l += '<a href="line-' + k + "-" + g[h][1] + '">' + g[h][0] + "</a>"
                    }
                }
            }
            return l
        },
        _vacationHotClick: function(f) {
            var g = f,
            h = this;
            MGTOOL.addEvent(g, "mousedown",
            function(l) {
                MGTOOL.stopEvent(l);
                var i = MGTOOL.getTarget(l),
                k = i.nodeName;
                if (i && k === "A") {
                    if (i.className === "noflag") {
                        h.onClick.fire({
                            type: "hotCommonClick",
                            target: h.target,
                            hideinput: h.hideInput,
                            node: i
                        });
                        h.firstFocus = false;
                        h.target.blur();
                        return
                    } else {
                        window.location = i.href
                    }
                }
            })
        },
        _vacationClick: function(g) {
            var f = MGTOOL.getbyClass(g, "div", "hotpanel"),
            h = this;
            if (!this.PP) {
                this.PP = (this.panelnum !== 0) ? f[this.panelnum] : f[0]
            }
            MGTOOL.addEvent(this.PP, "mousedown",
            function(l) {
                MGTOOL.stopEvent(l);
                var i = MGTOOL.getTarget(l),
                k = i.nodeName;
                if (i && k === "A") {
                    if (i.className === "noflag") {
                        h.onClick.fire({
                            type: "hotCommonClick",
                            target: h.target,
                            hideinput: h.hideInput,
                            node: i
                        });
                        h.firstFocus = false;
                        h.target.blur();
                        return
                    } else {
                        window.location = i.href
                    }
                }
            })
        },
        addVacation: function(g, i, h) {
            if (!g) {
                return false
            }
            if (!i) {
                return false
            }
            var l = panel = "";
            panel = '<div class="hotpanel">';
            for (name in i) {
                var k = name.split("|");
                panel += "<h2>" + k[0] + '</h2><div class="hotlist">' + this._getVacationData(i[name]) + "</div>"
            }
            g.className = "cityWrap hotWrap";
            var f = '<p class="cptip">拼音支持首字母输入，或 &larr; &uarr; &darr; &rarr; 选择</p>';
            g.innerHTML = f + panel;
            this.target.select();
            this._vacationHotClick(g)
        },
        addHotVacation: function(i, l, k) {
            if (!i) {
                return false
            }
            if (!l) {
                return false
            }
            var n = panel = "";
            for (name in l) {
                var m = name.split("|");
                n += '<span id="#' + m[1] + '">' + m[0] + "</span>";
                panel += '<div id="' + m[1] + '" class="hotpanel hothidden"><div class="hotlist">' + this._getVacationData(l[name]) + "</div></div>"
            }
            i.className = "cityWrap hotWrap";
            var f = '<p class="hottip">拼音支持首字母输入，或 &larr; &uarr; &darr; &rarr; 选择</p>',
            h = '<div id="hotnav" class="hotnav">' + n + "</div>";
            i.innerHTML = f + h + panel;
            this.target.select();
            var g = MGTOOL.$id("hotnav");
            this._hotTab(g, i);
            this._vacationClick(i)
        },
        addHotCommon: function(k, q, o) {
            if (!k) {
                return false
            }
            var f = panel = "";
            if (this.group.length) {
                var n = this.group.length;
                for (var m = 0; m < n; m++) {
                    f += '<span id="#' + this.group[m] + '">' + this.group[m] + "</span>";
                    panel += '<div id="' + this.group[m] + '" class="hotpanel hothidden">' + this._getPanelData(this.group[m], o) + "</div>"
                }
            }
            k.className = "cityWrap hotWrap";
            var p = '<p class="hottip">拼音支持首字母输入，或 &larr; &uarr; &darr; &rarr; 选择</p>',
            g = '<div id="hotnav" class="hotnav"><span id="#HOT">热门</span>' + f + "</div>",
            h = '<div id="HOT" class="hotpanel hothidden"><div class="hotlist">' + this._getHotList(q) + "</div></div>" + panel;
            k.innerHTML = p + g + h;
            this.target.select();
            var l = MGTOOL.$id("hotnav");
            this._hotTab(l, k);
            this._panelClick(k)
        },
        _panelClick: function(g) {
            var f = MGTOOL.getbyClass(g, "div", "hotpanel"),
            h = this;
            if (!this.PP) {
                this.PP = (this.panelnum !== 0) ? f[this.panelnum] : f[0]
            }
            MGTOOL.addEvent(this.PP, "mousedown",
            function(l) {
                MGTOOL.stopEvent(l);
                var i = MGTOOL.getTarget(l),
                k = i.nodeName;
                if (i && k === "A") {
                    h.onClick.fire({
                        type: "hotCommonClick",
                        target: h.target,
                        hideinput: h.hideInput,
                        node: i
                    });
                    h.firstFocus = false;
                    h.target.blur();
                    return
                }
                h.firstFocus = true
            })
        },
        _keyUpDown: function(r, k) {
            var q = k,
            f = r,
            l = [],
            g = [],
            o,
            s,
            n,
            h;
            g = f.getElementsByTagName("a");
            o = g.length;
            l = MGTOOL.getbyClass(f, "a", "current");
            if (!l.length) {
                return false
            }
            s = l[0];
            for (var m = 0; m < o; m++) {
                MGTOOL.removeClass(g[m], "current")
            }
            n = MGTOOL.next(s);
            h = MGTOOL.prev(s);
            switch (q) {
            case "up":
                if (h && h.style.display === "block") {
                    MGTOOL.removeClass(s, "current");
                    MGTOOL.addClass(h, "current")
                } else {
                    if (o > this.curList.length) {
                        MGTOOL.addClass(g[this.curList.length - 1], "current")
                    } else {
                        MGTOOL.addClass(g[o - 1], "current")
                    }
                }
                break;
            case "down":
                if (n && n.style.display === "block") {
                    MGTOOL.removeClass(s, "current");
                    MGTOOL.addClass(n, "current")
                } else {
                    MGTOOL.addClass(g[0], "current")
                }
                break
            }
        },
        _prevPage: function() {
            if (this.page > 1) {
                this.page--;
                this._setPage()
            }
        },
        _nextPage: function() {
            if (this.page < this.maxpage) {
                this.page++;
                this._setPage()
            }
        },
        _sliceStr: function(i, k) {
            var h = new RegExp(i, "i"),
            g = (k.length <= i.length) ? k.length: i.length;
            var f = h.test(k.substring(0, g));
            return f
        },
        _createKeyData: function() {
            if (this.keyWrap && !MGTOOL.$id("keytitle") && !MGTOOL.$id("keydata")) {
                this.keyWrap.innerHTML = '<div id="keytitle" class="keytitle"></div><div id="keydata" class="keydata"><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a><a href="#"><em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em></a></div><p id="key_page" class="key_page"><a id="m_p0" href="javascript:void(0)">&lt;--</a><a id="m_p1" href="javascript:void(0)">1</a><a id="m_p2" href="javascript:void(0)">2</a><a id="m_p3" href="javascript:void(0)">3</a><a id="m_p4" href="javascript:void(0)">4</a><a id="m_p5" href="javascript:void(0)">5</a><a id="m_p6" href="javascript:void(0)">--&gt;</a></p>'
            }
        },
        commenInputChange: function(o) {
            var g = o.wrap,
            r = o.str,
            l = arr = "",
            k = j = 0,
            p = o.cityDatas,
            u = o.hotDatas,
            h = o.hotDatas.length,
            n = o.cityDatas.length;
            var m = MGTOOL.$id("keytitle"),
            t = MGTOOL.$id("keydata"),
            f = MGTOOL.$id("key_page");
            if (!n) {
                m.innerHTML = "抱歉，没有找到您想要的城市";
                t.style.display = "none";
                f.style.display = "none";
                return
            }
            m.innerHTML = r + ",按拼音排序";
            t.style.display = "block";
            var q = r.length,
            s = [];
            for (; k < n; k++) {
                if (this._sliceStr(r, p[k][2])) {
                    s.push(p[k][0] + "|" + p[k][1] + '|<span class="red">' + r.toUpperCase() + "</span>" + p[k][2].slice(q))
                } else {
                    if (this._sliceStr(r, p[k][3]) && q >= 2) {
                        s.push(p[k][0] + "|" + p[k][1] + "|" + p[k][2])
                    } else {
                        if (this._sliceStr(r, p[k][0]) && q === 3) {
                            s.push(p[k][0] + "|" + p[k][1] + "|" + p[k][2])
                        } else {
                            if (p[k][1].indexOf(r) > -1) {
                                s.push(p[k][0] + "|" + p[k][1] + "|" + p[k][2])
                            }
                        }
                    }
                }
            }
            if (!s.length) {
                m.innerHTML = "抱歉，找不到" + r + "";
                t.style.display = "none";
                f.style.display = "none";
                return
            }
            this.allList = s;
            this.page = 1;
            this._setPage()
        },
        vacationInputChange: function(o) {
            var g = o.wrap,
            r = o.str,
            l = arr = "",
            k = j = 0,
            p = o.cityDatas,
            u = o.hotDatas,
            h = o.hotDatas.length,
            n = o.cityDatas.length;
            var m = MGTOOL.$id("keytitle"),
            t = MGTOOL.$id("keydata"),
            f = MGTOOL.$id("key_page");
            if (!n) {
                m.innerHTML = "";
                m.style.display = "none";
                t.style.display = "none";
                f.style.display = "none";
                return
            }
            m.innerHTML = r + ",按拼音排序";
            m.style.display = "block";
            t.style.display = "block";
            var q = r.length,
            s = [];
            for (; k < n; k++) {
                if (this._sliceStr(r, p[k][0])) {
                    s.push(p[k][2] + "|" + p[k][3] + '|<span class="red">' + r + "</span>" + p[k][0].slice(q))
                } else {
                    if (this._sliceStr(r, p[k][1])) {
                        s.push(p[k][2] + "|" + p[k][3] + "|" + p[k][0])
                    } else {
                        if (this._sliceStr(r, p[k][3])) {
                            s.push(p[k][2] + "|" + p[k][3] + "|" + p[k][0])
                        }
                    }
                }
            }
            if (!s.length) {
                m.innerHTML = "";
                m.style.display = "none";
                t.style.display = "none";
                f.style.display = "none";
                return
            }
            this.allList = s;
            this.page = 1;
            this._setPage()
        },
        _keyDown: function(i) {
            var h = MGTOOL.getkeycode(i);
            var f = MGTOOL.$id("keydata");
            switch (h) {
            case 13:
                MGTOOL.preventDefault(i);
                var g = MGTOOL.getbyClass(f, "a", "current");
                if (g.length) {
                    this.onKey.fire({
                        type: "keyEnter",
                        target: this.target,
                        hideinput: this.hideInput,
                        node: g[0]
                    });
                    this.target.blur();
                    MGTOOL.preventDefault(i)
                }
                break;
            case 27:
                this.target.blur();
                this.target.value = "";
                break;
            case 37:
                this._prevPage();
                break;
            case 38:
                this._keyUpDown(f, "up");
                break;
            case 39:
                this._nextPage();
                break;
            case 40:
                this._keyUpDown(f, "down");
                break
            }
        },
        _keyClick: function() {
            var k = MGTOOL.$id("key_page");
            if (!k) {
                return
            }
            var h = k.getElementsByTagName("a"),
            f = h.length;
            var l = this;
            for (var g = 0; g < f; g++) {
                h[g].onmousedown = function(i) {
                    if (window.addEventListener) {
                        MGTOOL.stopEvent(i)
                    }
                    switch (this.id) {
                    case "m_p0":
                        l._prevPage();
                        break;
                    case "m_p6":
                        l._nextPage();
                        break;
                    default:
                        l.page = this.firstChild.nodeValue;
                        l._setPage()
                    }
                }
            }
        },
        _setData: function(l) {
            var h = l.getElementsByTagName("a"),
            f = h.length;
            var k = f;
            while (k--) {
                if (k < this.curList.length) {
                    var g = this.curList[k].split("|");
                    h[k].style.display = "block";
                    h[k].href = "#" + g[0];
                    h[k].firstChild.innerHTML = g[1];
                    h[k].lastChild.innerHTML = g[2]
                } else {
                    h[k].style.display = "none";
                    h[k].innerHTML = '<em class="mg_c_name">&nbsp;</em><em class="mg_c_pinyin">&nbsp;</em>'
                }
            }
        },
        _setPage: function() {
            var g = this.allList.length,
            h = Math.ceil(g / this.listNum);
            this.maxpage = h;
            if (g < this.listNum) {
                this.curList = this.allList;
                this.maxpage = 0
            } else {
                this.curList = this.allList.slice(this.listNum * (this.page - 1), (g - this.listNum * (this.page - 1) < this.listNum ? g: this.listNum * this.page))
            }
            var f = MGTOOL.$id("keydata");
            this._setData(f);
            this._setFirstClass(f, "a", "current");
            this._setPageNum();
            this.keyDataClick(f)
        },
        _setPageNum: function() {
            var f = MGTOOL.$id("m_p0"),
            k = MGTOOL.$id("m_p6"),
            n = [MGTOOL.$id("m_p1"), MGTOOL.$id("m_p2"), MGTOOL.$id("m_p3"), MGTOOL.$id("m_p4"), MGTOOL.$id("m_p5")];
            var h = this.maxpage < 5 || this.page < 3 ? 1 : this.page > this.maxpage - 2 ? this.maxpage - 4 : this.page - 2;
            var g = Math.min(h + 4, this.maxpage);
            var m;
            f.style.display = this.page > 1 ? "inline": "none";
            k.style.display = this.page == this.maxpage ? "none": "inline";
            for (var l = h; l < h + 5; l++) {
                m = n[l - h];
                if (l <= g) {
                    m.firstChild.nodeValue = l;
                    m.className = l == this.page ? "address_current": "";
                    m.style.display = "inline"
                } else {
                    m.style.display = "none"
                }
            }
            MGTOOL.$id("key_page").style.display = this.maxpage > 1 ? "block": "none"
        },
        _setFirstClass: function(l, g, m) {
            if (!l) {
                return false
            }
            var h = l.getElementsByTagName(g);
            if (!h.length) {
                return false
            }
            for (var k = 0,
            f = h.length; k < f; k++) {
                MGTOOL.removeClass(h[k], m)
            }
            MGTOOL.addClass(h[0], m)
        },
        _focus: function() {
            if (this.target.value == this.inputDefaultValue) {
                this.target.value = ""
            }
            this._createKeyData();
            this._oninputchage(this.target);
            if (!this.keyhide) {
                this.keyhide = true;
                return
            }
            if (this.firstFocus) {
                this.firstFocus = false;
                return
            }
            if (this.keyWrap) {
                this.keyWrap.style.dislay = "none"
            }
            this.onFocus.fire({
                type: "onFirst",
                parentBox: this.wrap,
                hotlist: this.hotDatas,
                datalist: this.cityDatas
            });
            if (this.hotPop) {
                this.hotPop.setPos()
            }
            var f = this;
            MGTOOL.addEvent(this.wrap, "mousedown",
            function(g) {
                f.firstFocus = true;
                var h = setTimeout(function() {
                    f.firstFocus = false
                },
                20);
                return false
            })
        },
        _blur: function() {
            var f = this;
            if (this.firstFocus) {
                this.firstFocus = false;
                setTimeout(function() {
                    f.target.focus()
                },
                0);
                return
            }
            if (this.hotPop) {
                this.hotPop.resetPost()
            }
            this.wrap.onmousedown = null;
            if (!this.keyPop) {
                return false
            }
            if (!this.keyhide) {
                setTimeout(function() {
                    f.target.focus()
                },
                0);
                return
            }
            this.keyPop.resetPost();
            this.keyhide = true;
            if (!this.target.value.length || this.trim(this.target.value) == "") {
                this.target.value = this.inputDefaultValue
            } else {
                f.onFocus.fire({
                    type: "toblur",
                    target: f.target,
                    hideinput: f.hideInput
                })
            }
        },
        keyDataClick: function(g) {
            var g = g;
            var h = g.getElementsByTagName("a"),
            k = 0,
            f = h.length;
            var l = this;
            for (; k < f; k++) {
                h[k].onmousedown = function(i) {
                    MGTOOL.stopEvent(i);
                    l.onKey.fire({
                        type: "keyClick",
                        target: l.target,
                        hideinput: l.hideInput,
                        node: this
                    });
                    l.target.blur();
                    l.keyhide = true
                }
            }
        },
        inputChangeSet: function() {
            if (!this.keyWrap) {
                return false
            }
            if (this.wrap) {
                this.wrap.style.display = "none"
            }
            if (this.firstFocus) {
                this.firstFocus = false;
                return
            }
            this.keyWrap.className = "cityWrap keylist";
            var h = this.trim(this.target.value);
            if (!h.length) {
                if (this.wrap) {
                    this.wrap.style.display = "block"
                }
                if (this.keyPop) {
                    this.keyPop.resetPost()
                }
                this.firstFocus = false;
                return
            }
            if (h === this.inputDefaultValue) {
                return false
            }
            this.onKey.fire({
                type: "inputChange",
                wrap: this.keyWrap,
                str: h,
                hotDatas: this.hotDatas,
                cityDatas: this.cityDatas
            });
            var f = MGTOOL.$id("keydata");
            if (!f) {
                return
            }
            if (this.keyPop) {
                this.keyPop.setPos()
            }
            this._keyClick();
            var g = this;
            MGTOOL.addEvent(this.keyWrap, "mousedown",
            function(i) {
                g.keyhide = false
            })
        },
        _oninputchage: function(g) {
            var f = this;
            if ("onpropertychange" in g) {
                g.onpropertychange = function() {
                    if (window.event.propertyName == "value") {
                        f.inputChangeSet()
                    }
                }
            } else {
                g.addEventListener("input",
                function() {
                    f.inputChangeSet()
                },
                false)
            }
        },
        _bindEvent: function() {
            if (!this.target) {
                return false
            }
            var f = this;
            MGTOOL.addEvent(this.target, "focus",
            function() {
                f._focus()
            });
            MGTOOL.addEvent(this.target, "blur",
            function() {
                f._blur()
            });
            MGTOOL.addEvent(this.target, "keydown",
            function(g) {
                f._keyDown(g)
            })
        }
    };
    MGTOOL.citySelecter = b;
    MGTOOL.warn = c;
    MGTOOL.popSelecter = d
})();