function getBgPath(src) {
    if (app.$data.transition_el02 === 'com_friend' && app.$data.transition_el_type === 2) {
        app.$data.chat_bg = 'bg';
        $(".bgimg_friend").prop("src", src);
    } else {
        app.$data.allData.my_info.chat_bg = src;
        app.update(app.$data.allData.my_info, 'my_info', id);
        app.$toast({
            message: '聊天背景已替换',
            position: 'bottom',
            duration: 2000
        });
    }
}

function initVue() {
    app = new Vue({
        el: '#app',
        data: function() {
            return {
                /******************wechat*******************/
                tab_index: 0,
                //开始按压时间
                touch_starttime: null,
                // 按压的好友索引
                touch_index: null,
                wechat_css: null,
                //定时器
                timer: null,
                //长按好友列表弹出框
                sheetVisible: false,
                actions: [{
                    name: '标记未读',
                    method: function() {
                        // app.alertInputMsg('设置未读数量', function() {
                        //     app.chatlist[app.$data.touch_index].msg_num = parseInt($(".tip_modal_input").val())
                        // })
                        app.$messagebox({
                            title: '提示',
                            message: '设置未读数量',
                            showCancelButton: true,
                            inputType: 'number',
                            showInput: true,
                        });
                        $(".mint-msgbox").css("top", '30%');
                        app.$messagebox.prompt('设置未读数量').then(function(obj, action) {
                            $(".mint-msgbox").css("top", '50%');
                            value = parseInt(obj.value)
                            if (value < 0 || !value) {
                                value = 0
                            }
                            app.$data.current_friend.msg_num = value
                            app.update(app.$data.allData.allfriend, 'allfriend', id)
                        }, function() {
                            $(".mint-msgbox").css("top", '50%');
                        });
                    }
                }, {
                    name: '设置时间',
                    method: function() {
                        app.openPicker('set_time', app.$data.current_friend.com_time)
                    }
                }, {
                    name: '删除',
                    method: function() {
                        var _this = this
                        app.$messagebox({
                            title: '提示',
                            message: '亲爱的，删除后将清空聊天记录，确定要删除吗？',
                            showCancelButton: true
                        })
                        app.$messagebox.confirm('亲爱的，删除后将清空聊天记录，确定要删除吗？').then(function(action) {
                            app.$data.current_friend.is_chatlist = -1;
                            app.$data.current_friend.chat_record = [];
                            app.update(app.$data.allData.allfriend, 'allfriend', id)
                        });
                    }
                }],
                //视频聊天时长
                video_chat_time: '',
                video_chat_actions: [{
                    name: '已接通',
                    method: function() {
                        app.$data.video_chat_state = '已接通'
                    }
                }, {
                    name: '已取消',
                    method: function() {
                        app.$data.video_chat_state = '已取消'
                    }
                }, {
                    name: '已拒绝',
                    method: function() {
                        app.$data.video_chat_state = '已拒绝'
                    }
                }],
                //视频聊天状态弹出框
                video_chat_sheetVisible: false,
                video_chat_state: '已接通',
                //视频聊天类型
                video_chat_value: '视频聊天',
                //图片消息集合
                pic_msg_arr: [],
                //当前红包详情
                zz_info: {},
                //当前转账详情
                hb_info: {},
                //时间聊天消息
                chat_pickerVisible: false,
                //语音消息时长
                rangeValue: '1',
                //文本输入框显示隐藏
                chat_input_key: true,
                //表情图标显示隐藏
                face_tools_key: false,
                //加号图标显示隐藏
                add_tools_key: false,
                //发送按钮显示隐藏
                send_btn_key: false,
                //键盘弹起高度
                footer_bot: 0,
                //聊天背景弹出框
                bg_sheetVisible: false,
                bg_actions: [{
                        name: '自定义背景',
                        method: function() {
                            vjt.getBgImg();
                        }
                    },
                    {
                        name: '默认背景',
                        method: function() {
                            if (app.$data.transition_el02 === 'com_friend' && app.$data.transition_el_type === 2) {
                                app.$data.chat_bg = 'default';
                            } else {
                                app.$data.allData.my_info.chat_bg = '';
                                app.update(app.$data.allData.my_info, 'my_info', id)
                                app.$toast({
                                    message: '聊天背景已还原',
                                    position: 'bottom',
                                    duration: 2000
                                });
                            }
                        }
                    }
                ],
                // 是否添加打招呼消息
                is_beenadd: false,
                //添加好友时间和接受请求时间
                add_chat_time: new Date(),
                js_chat_time: new Date(),
                // 时间选择器
                pickerVisible: false,
                add_zz_pickerVisible: false,
                //（新建聊天，时间消息，转账消息）的开始时间和结束时间
                startDate: new Date(),
                endDate: new Date(),
                chat_startDate: new Date(),
                chat_endDate: new Date(),
                zz_time_startDate: new Date(),
                touch_msg_starttime: null,
                touch_msg_index: null,
                timer_msg: null,
                //点击朋友圈弹出框
                sheetVisible_msg: false,
                actions_msg: [{
                    name: "删除消息",
                    method: function() {
                        app.$data.current_friend.chat_record.splice(app.$data.touch_msg_index, 1)
                        app.update(app.$data.allData.allfriend, 'allfriend', id)
                    }
                }],

                /*******************通讯录********************/
                mailsheetVisible: false,
                actions_mail: [{
                    name: "清空通讯录",
                    method: function() {
                        app.$messagebox({
                            title: '提示',
                            message: '亲爱的，确定要清空通讯录吗？',
                            showCancelButton: true,
                        });
                        app.$messagebox.confirm('亲爱的，确定要清空通讯录吗？').then(function(action) {
                            app.$data.allData.allfriend = [];
                            app.update(app.$data.allData.allfriend, 'allfriend', id)
                        });
                    }
                }, {
                    name: "删除",
                    method: function() {
                        app.$messagebox({
                            title: '提示',
                            message: '亲爱的，删除该联系人后将清空相关聊天记录，确定要删除吗？',
                            showCancelButton: true,
                        });
                        app.$messagebox.confirm('亲爱的，删除该联系人后将清空相关聊天记录，确定要删除吗？').then(function(action) {
                            var remove_id = app.$data.current_friend.id
                            $(app.$data.allData.allfriend).each(function(i, v) {
                                $(v.mail_list).each(function(index, obj) {
                                    if (obj.id === remove_id) {
                                        v.mail_list.splice(index, 1)
                                        if (v.mail_list.length === 0) {
                                            app.$data.allData.allfriend.splice(i, 1)
                                        }
                                        return false;
                                    }
                                })
                            })
                            app.update(app.$data.allData.allfriend, 'allfriend', id)
                        });
                    }
                }],
                //通讯录添加朋友框显示隐藏
                transition_box_head_modal_key: false,
                // 是否直接通过好友请求
                is_newfriend: false,


                /**********朋友圈*********** */
                //朋友圈数据
                friend_q: {
                    num: 0,
                    show: false,
                    img_src: ''
                },
                //点击朋友圈弹出框
                sheetVisible_friend: false,
                actions_friend_q: [{
                    name: "设置朋友圈动态数量",
                    method: function() {
                        app.$messagebox({
                            title: '提示',
                            message: '设置动态数量',
                            showCancelButton: true,
                            inputType: 'number',
                            showInput: true,
                        });
                        $(".mint-msgbox").css("top", '30%');
                        app.$messagebox.prompt('设置朋友圈动态数量').then(function(obj) {
                            $(".mint-msgbox").css("top", '50%');
                            value = parseInt(obj.value)
                            if (value > 99) {
                                value = '...'
                            } else if (value < 0 || !value) {
                                value = 0
                            }
                            app.$data.friend_q.num = value
                        }, function() {
                            $(".mint-msgbox").css("top", '50%');
                        });
                    }
                }, {
                    name: "切换朋友圈动态图片",
                    method: function() {
                        app.$data.friend_q.show = !app.$data.friend_q.show
                        if (app.$data.friend_q.show) {
                            app.$data.friend_q.img_src = vjt.getImg()
                        }
                    }
                }],

                // {
                //     type: 'H',
                //     mail_list: [{
                //         name: '哈哈',
                //         is_chatlist: 1,
                //         id: 1,
                //         icon: './img/icon.png',
                //         msg_num: '1',
                //         chat_bg: '',
                //         com_time: '2017-11-20 9:34:04',
                //         chat_record: [{
                //             isMe: true,
                //             msg_type: 'pic',
                //             text: './images/banner.png',
                //             time01: '200',
                //             time02: '100'
                //         }],
                //         request_msg: '你好',
                //         request_state: true
                //     }]
                // }
                //*************通用********************* */
                bg_el: "",
                allData: {
                    new_friend_list: [],
                    allfriend: [],
                    my_info: {
                        tx: './img/icon.png',
                        my_nc: 'LZF',
                        small_change: 0,
                        my_wxh: 'liuzhenfeng2010',
                        chat_bg: ''
                    }
                },
                cu_time_func: 'set_time',
                time_func: {
                    'add_chat': function(time) { //添加聊天时间
                        app.$data.add_chat_time = time
                    },
                    'js_chat': function(time) { //接受聊天时间
                        app.$data.js_chat_time = time
                    },
                    'set_time': function(time) { //设置时间
                        app.$data.current_friend.com_time = time
                    }
                },
                // 当前聊天人
                current_friend: {},
                // 当前展示的元素
                transition_el: null,
                transition_el_type: 0,
                old_transition_el03: null,
                transition_el03: null,
                old_transition_el02: null,
                transition_el02: null,
                old_transition_el: null,
                //右上角弹出框显示隐藏
                head_modal_key: false,
                weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
                isMe: false,
                //点击随机相册元素
                xc_el: '',
                //相册弹出框显示隐藏
                xc_sheetVisible: false,
                xc_actions: [{
                        name: '随机一个',
                        method: function() {
                            if (app.$data.transition_el === 'set_my_info') {
                                app.$data.allData.my_info.tx = vjt.getImg()
                                app.update(app.$data.allData.my_info, 'my_info', id)
                            } else {
                                $(app.$data.xc_el).prop("src", vjt.getImg())
                            }
                        }
                    },
                    {
                        name: '相册',
                        method: function() {
                            vjt.getPhoto();
                        }
                    }
                ],
                tip_input_modal_key: false,
                tip_info_modal_key: false,
                scroll: null,
                chat_bg: ''
            }
        },
        mounted: function() {
            // this.$nextTick(function() {
            //     var scroll = new BScroll('.wechat_content', {
            //         scrollY: true,
            //         click: true,
            //         tap: true,
            //     })
            //     var scroll1 = new BScroll('#maillist', {
            //         scrollY: true,
            //         click: true,
            //         tap: true,
            //     })
            //     var scroll2 = new BScroll('.add_friend_wrapper', {
            //         scrollY: true,
            //         click: true,
            //         tap: true,
            //     })
            //     var scroll3 = new BScroll('.chat_window_body', {
            //         scrollY: true,
            //         click: true,
            //         tap: true,
            //     })
            // })

            // 初始化表情
            this.initFace()
                // this.current_friend = new Friend();
        },
        filters: {
            videoTextFilter: function(item) {
                var state = item.text,
                    isMe = item.isMe;
                if (state === '已接通') {
                    state = '聊天时长 ' + item.time;
                } else if (state === '已取消' && isMe === false) {
                    state = '对方已取消';
                } else if (state === '已拒绝' && isMe === true) {
                    state = '对方已拒绝';
                }
                return state;
            },
            //语音长度
            audio_filter: function(num) { //全局方法 Vue.filter() 注册一个自定义过滤器,必须放在Vue实例化前面
                var baseWidth = 86;
                var rtnWidth = 0;
                if (num == 1 || num == 2) {
                    rtnWidth = baseWidth;
                } else if (num <= 10) {
                    rtnWidth = baseWidth + Number(num - 2) * 8
                } else if (num > 10 && num < 20) {
                    rtnWidth = baseWidth + 8 * 8;
                } else if (num >= 20 && num < 30) {
                    rtnWidth = baseWidth + 9 * 8;
                } else if (num >= 30 && num < 40) {
                    rtnWidth = baseWidth + 10 * 8;
                } else if (num >= 40 && num < 50) {
                    rtnWidth = baseWidth + 11 * 8;
                } else if (num >= 50 && num < 60) {
                    rtnWidth = baseWidth + 12 * 8;
                } else if (num == 60) {
                    rtnWidth = baseWidth + 12 * 8 + 20;
                }
                return "width:" + rtnWidth + 'px';
            },
            //格式化金额,自动补零
            returnFloat: function(value) {
                var value = Math.round(parseFloat(value) * 100) / 100;
                var xsd = value.toString().split(".");
                if (xsd.length == 1) {
                    value = value.toString() + ".00";
                    return value;
                }
                if (xsd.length > 1) {
                    if (xsd[1].length < 2) {
                        value = value.toString() + "0";
                    }
                    return value;
                }
            },
            // 聊天列表时间
            gshTimeFilter: function(time) {
                return app.gshTime(time);
            },
            gshChatTimeFilter: function(time) {
                return app.gshChatTime(time);
            }
        },
        computed: {
            chatlist: function() { //聊天列表数据
                var arr = []
                $(this.allData.allfriend).each(function(i, val) {
                    $(val.mail_list).each(function(i, obj) {
                        if (obj.is_chatlist !== -1) {
                            arr.push(obj)
                        }
                    })
                })
                return arr
            },
            //总好友个数
            alldate_len: function() {
                var num = 0
                this.allData.allfriend.forEach(function(val) {
                    num += val.mail_list.length
                })
                return num;
            },
            // 头部未读消息总数
            weidu_num: function() {
                var num = 0;
                this.chatlist.forEach(function(obj, index) {
                    num += obj.msg_num
                })
                return num;
            },
            //添加好友时间
            add_time: function() {
                return this.reTime02(this.add_chat_time)
            },
            //接受好友时间
            js_time: function() {
                return this.reTime02(this.js_chat_time)
            }
        },
        watch: {
            // current_friend: function() {
            //     if (this.current_friend.chat_bg) {
            //         $(".chat_window").css('backgroundImage', 'url(' + this.current_friend.chat_bg + ')')
            //     } else if (this.allData.my_info.chat_bg) {
            //         $(".chat_window").css('backgroundImage', 'url(' + this.allData.my_info.chat_bg + ')')
            //     } else {
            //         $(".chat_window").css('backgroundImage', 'none')
            //     }
            // },
            current_friend: function() {
                var _this = this;
                if (this.transition_el === 'chat_window' && this.transition_el_type === 1) {
                    return;
                }
                this.$nextTick(function() {

                })
            },
            chatlist: function() {
                app.updateSort();
            }
        },
        updated(){
           $(".wechat_title_wen").text($(".footer_nav_item.active>p").text())
           if($(".wechat_title_wen").text()=="我"){
               $(".head_tool_search").css({"display":"none"});
               $(".wechat_title_wen").text("")
               $(".header").css({"backgroundColor":"#fff"})
               $(".head_tool_add").css("backgroundImage","url('./img/icon-add2.png')")
           }else{
            $(".head_tool_search").css({"display":"inline-block"});
            $(".header").css({"backgroundColor":"#fff"})
            $(".head_tool_add").css("backgroundImage","url('./img/icon-add.png')")
           }
        },

        methods: {
            // 点击菜单栏切换
        
            //*************聊天列表和聊天窗*******************/
            currencyChatBg: function() {
                this.chat_bg = '';
            },
            disComFriend: function() {
                this.switchPage('com_friend', 2)
                $(".com_friend_nc").val(this.current_friend.name)
                $(".com_friend_img").prop("src", this.current_friend.icon)

                if (this.current_friend.chat_bg == 'default') { // 背景图片变成默认背景图
                    this.chat_bg = 'default';
                } else if (!this.current_friend.chat_bg) { // 跟随通用
                    this.chat_bg = '';
                } else if (this.current_friend.chat_bg && (this.current_friend.chat_bg != 'default')) { // 相册图片
                    this.chat_bg = 'bg';
                }
            },
            // 清空当前聊天人的消息列表
            emptyRecordList: function() {
                var _this = this
                this.$messagebox({
                    title: '提示',
                    message: '亲爱的，确定清空吗？',
                    showCancelButton: true
                })
                this.$messagebox.confirm('亲爱的，确定清空吗？').then(function(action) {
                    _this.current_friend.chat_record = [];
                    _this.$toast({
                        message: '聊天记录已清空',
                        position: 'bottom',
                        duration: 2000
                    });
                    _this.update(_this.allData.allfriend, 'allfriend', id)
                });
            },
            // 保存联系人角色编辑
            comFriendInfo: function() {
                // 昵称
                if ($(".com_friend_nc").val() != "") {
                    this.current_friend.name = $(".com_friend_nc").val()
                    this.sortMailFriendList(this.current_friend.id)
                } else {
                    this.$toast({
                        message: '请输入昵称',
                        position: 'bottom',
                        duration: 2000
                    });
                    return;
                }

                // 头像
                this.current_friend.icon = $(".com_friend_img").prop("src")

                // 当聊天窗顶部名称过长时截取两端字符，中间显示省略号
                this.$nextTick(function() {
                    var chat_window_header_text = $(".chat_window_header .transition_box_head_text")
                    if (chat_window_header_text.width() > 288) {
                        chat_window_header_text.text(getNameSubStr(chat_window_header_text.text()))
                    }
                })

                // 背景图片
                if (this.chat_bg == 'default') { // 背景图片变成默认背景图
                    this.current_friend.chat_bg = 'default';
                } else if (!this.chat_bg) { // 跟随通用
                    this.current_friend.chat_bg = '';
                } else if (this.chat_bg && (this.chat_bg != 'default')) { // 相册图片
                    this.current_friend.chat_bg = $(".bgimg_friend").prop("src")
                }

                back()
                app.update(app.$data.allData.allfriend, 'allfriend', id)
            },
            //发送消息之后的更新数据
            updateMsg: function() {
                this.current_friend.is_chatlist = 1;
                this.current_friend.com_time = this.reTime02(new Date());
                this.updateSort();
                this.$nextTick(function() {
                    scrollBot();
                })
            },
            //长按聊天消息
            msgTouchstart: function(index) {
                this.touch_msg_starttime = new Date().getTime();
                this.touch_msg_index = index;
                this.timer_msg = setTimeout(function() {
                    //长按之后执行
                    app.$data.sheetVisible_msg = true;
                }, 700)
            },
            msgTouchMove: function() {
                clearTimeout(this.timer_msg)
                this.timer_msg = null
            },
            msgTouchEnd: function() {
                var touch_endtime = new Date().getTime()
                if (!(this.timer_msg && (this.timer_msg !== 0)))
                    return;
                if (touch_endtime - this.touch_msg_starttime < 700) {
                    clearTimeout(this.timer_msg)
                }
            },
            // 发送文字消息
            sendMsgText: function(target) {
                var msg_text = new Msg();
                msg_text.text = $("#chat_input").html()
                msg_text.msg_type = 'text'
                msg_text.isMe = this.isMe ? true : false
                this.current_friend.chat_record.push(msg_text)
                this.send_btn_key = false
                $("#chat_input").empty()
                $("#chat_input").focus()
                this.chatInputFocus()
                this.updateMsg()
                this.update(this.allData.allfriend, 'allfriend', id)
            },
            // 点击头部切换发送对象
            switchChatTarget: function() {
                this.isMe = !this.isMe
                if (this.isMe) {
                    this.$toast({
                        message: '已切换为【自己】说话',
                        position: 'bottom',
                        duration: 2000
                    });
                } else {
                    this.$toast({
                        message: '已切换为【对方】说话',
                        position: 'bottom',
                        duration: 2000
                    });
                }
            },
            // 添加时间消息
            openChatPicker: function() {
                this.chat_startDate = new Date()
                this.$refs.chat_picker.open()
            },
            addMsgTime: function(time) {
                var msg_time = new Msg();
                msg_time.msg_type = 'system_time';
                msg_time.time_date = time;
                msg_time.time = this.reTime(time);
                this.current_friend.chat_record.push(msg_time);
                this.updateMsg();
                this.update(app.$data.allData.allfriend, 'allfriend', id);
            },
            openMsgYuyin: function() {
                this.rangeValue = '1';
                this.add_tools_key = false;
                this.switchPage('yuyin', 2);
            },
            addMsgYuyin: function() {
                var msg_audio = new Msg();
                msg_audio.audio_num = this.rangeValue;
                msg_audio.msg_type = 'audio';
                msg_audio.isMe = this.isMe;
                this.current_friend.chat_record.push(msg_audio);
                back();
                this.updateMsg();
                this.update(app.$data.allData.allfriend, 'allfriend', id);
            },
            xcPicMsg: function() {
                if (this.pic_msg_arr.length >= 9) {
                    this.$toast({
                        message: '你最多只能选择9个图片',
                        position: 'bottom',
                        duration: 2000
                    })
                } else {
                    vjt.getWechatPic();
                }
            },
            openMsgPic: function() {
                this.switchPage('pic', 2);
                this.add_tools_key = false;
                this.pic_msg_arr = [];
                $(".new_msg_pic").prop("src", '');
            },
            addMsgPic: function() {
                var len = this.pic_msg_arr.length;
                if (len === 0) {
                    this.$toast({
                        message: '请选择图片',
                        position: 'bottom',
                        duration: 2000
                    })
                    return;

                } else if (len >= 1) {
                    // var msg_pic = new Msg();
                    // msg_pic.msg_type = 'pic';
                    // msg_pic.text = this.pic_msg_arr[0].src;
                    // msg_pic.isMe = this.isMe;
                    // msg_pic.time01 = this.pic_msg_arr[0].w;
                    // msg_pic.time02 = this.pic_msg_arr[0].h;
                    // this.current_friend.chat_record.push(msg_pic);
                    // } else {
                    $(this.pic_msg_arr).each(function(i, v) {
                        var msg_pic = new Msg();
                        msg_pic.msg_type = 'pic';
                        msg_pic.text = v.src;
                        msg_pic.time01 = v.w;
                        msg_pic.time02 = v.h;
                        msg_pic.isMe = app.$data.isMe;
                        app.$data.current_friend.chat_record.push(msg_pic);
                    })
                }
                back()
                this.updateMsg()
                this.update(app.$data.allData.allfriend, 'allfriend', id)
            },
            openMsgZz: function() {
                this.switchPage('transfer_accounts', 2)
                this.add_tools_key = false
                $("#new_msg_zz_pri,#new_msg_zz_msg").val("")
                $("#new_msg_zz_time").text(this.reTime(new Date()))
            },
            zzPriChange: function() {
                var val = $("#new_msg_zz_pri").val();
                if (val.length > 10) {
                    $("#new_msg_zz_pri").val(val.substring(0, 10));
                }
            },
            // 添加转账时间
            openZzTimePicker: function() {
                this.zz_time_startDate = new Date()
                this.$refs.add_zz_time_picker.open()
            },
            addZzTime: function(time) {
                $("#new_msg_zz_time").text(this.reTime(time))
            },
            addMsgZz: function() {
                var val = $("#new_msg_zz_pri").val();
                if (Number(val) <= 0 || (!val)) {
                    //如果金额为空或者小于等于0
                    this.$toast({
                        message: '请输入正确的金额',
                        position: 'bottom',
                        duration: 2000
                    })
                    return;
                }
                var msg_zz = new Msg()
                msg_zz.msg_type = 'msg_zz'
                msg_zz.isMe = this.isMe
                msg_zz.isReceive = false
                msg_zz.money = $("#new_msg_zz_pri").val()
                msg_zz.message = $("#new_msg_zz_msg").val() || ''
                msg_zz.time01 = $("#new_msg_zz_time").text()
                this.current_friend.chat_record.push(msg_zz)
                back()
                this.updateMsg()
                this.update(app.$data.allData.allfriend, 'allfriend', id)
            },
            openMsgHb: function() {
                this.switchPage('hongb', 2)
                this.add_tools_key = false
                $("#new_msg_hb_pri,#new_msg_hb_msg").val("")
            },
            addMsgHb: function() {
                var val = $("#new_msg_hb_pri").val();
                if (Number(val) <= 0 || (!val) || Number(val) > 200) {
                    //如果金额为空或者小于等于0
                    this.$toast({
                        message: '请输入金额,不能大于200',
                        position: 'bottom',
                        duration: 2000
                    })
                    return;
                }
                var msg_hb = new Msg()
                msg_hb.msg_type = 'msg_hb'
                msg_hb.money = parseInt($("#new_msg_hb_pri").val())
                msg_hb.message = $("#new_msg_hb_msg").val()
                msg_hb.isMe = this.isMe
                msg_hb.isReceive = false
                this.current_friend.chat_record.push(msg_hb)
                back()
                this.updateMsg()
                this.update(app.$data.allData.allfriend, 'allfriend', id)
            },
            openMsgVideo: function() {
                this.switchPage('video', 2)
                this.add_tools_key = false

                this.video_chat_state = '已接通';
                this.video_chat_sheetVisible = false;
                this.video_chat_value = '视频聊天';
                this.video_chat_time = '';
            },
            // 打开选择视频聊天状态框
            openVideoChatSate: function() {
                this.video_chat_sheetVisible = true;
            },
            // 确认设置视频聊天消息
            addMsgVideo: function() {
                var msg_video = new Msg()
                var time = this.videoTime();
                if ((!time) && this.video_chat_state === '已接通') {
                    this.$toast({
                        message: '填写正确时间格式',
                        position: 'bottom',
                        duration: 2000
                    })
                    return;
                }
                msg_video.isMe = this.isMe;
                msg_video.msg_type = 'video';
                msg_video.text = this.video_chat_state;
                msg_video.time = time;
                msg_video.message = this.video_chat_value;
                this.current_friend.chat_record.push(msg_video)
                this.updateMsg()
                back();
                this.update(app.$data.allData.allfriend, 'allfriend', id)
            },
            videoTime: function() {
                var time = this.video_chat_time;
                var index = time.indexOf('.')
                var time_arr = time.split('.');
                if (parseFloat(time) < 60 && parseFloat(time) > 0 && index != -1 && time_arr[1] < 60) {
                    if (index === 0) {
                        return '00:' + this.zeroFill(time_arr[1]);

                    } else if (index === time.length - 1) {
                        return this.zeroFill(time_arr[0]) + ':00';

                    } else {
                        return this.zeroFill(time_arr[0]) + ':' + this.zeroFill(time_arr[1])
                    }
                } else {
                    return false;
                }
            },
            focusVideoTime: function(){
                vjt.getKeyHeight()
            },

            // 确认收钱
            confirmMoney: function() {
                this.zz_info.isReceive = true
                var now_date = new Date()
                var year = now_date.getFullYear(),
                    month = now_date.getMonth() + 1,
                    date = now_date.getDate(),
                    hour = now_date.getHours(),
                    min = now_date.getMinutes()
                month = month < 10 ? '0' + month : month
                date = date < 10 ? '0' + date : date
                hour = hour < 10 ? '0' + hour : hour
                min = min < 10 ? '0' + min : min
                this.zz_info.time02 = year + '-' + month + '-' + date + ' ' + hour + ':' + min
                var msg_zz_ok = new Msg()
                msg_zz_ok.msg_type = 'msg_zz_ok'
                msg_zz_ok.isMe = this.zz_info.isMe
                msg_zz_ok.money = this.zz_info.money
                msg_zz_ok.time01 = this.zz_info.time01
                msg_zz_ok.time02 = this.zz_info.time02
                msg_zz_ok.isReceive = true
                this.current_friend.chat_record.push(msg_zz_ok)
                this.updateMsg()
                scrollBot()
                this.update(app.$data.allData.allfriend, 'allfriend', id)
            },
            // 打开转账框
            openZz: function(item) {
                this.zz_info = item

                this.switchPage('zz', 2)
            },
            // 打开红包框
            openHb: function(item) {
                if (item.isMe === this.isMe) {
                    this.$toast({
                        message: '点击头部昵称切换聊天对象',
                        position: 'bottom',
                        duration: 2000
                    })
                    return;
                }
                this.hb_info = item

                this.switchPage('hb', 2)

                if (!item.isReceive) {
                    item.isReceive = true
                    var sys_hb = new Msg()
                    sys_hb.msg_type = 'receive_hb'
                    item.isMe ? sys_hb.isMe = true : sys_hb.isMe = false
                    this.current_friend.chat_record.push(sys_hb)
                    this.updateMsg() //更新聊天时间和排序
                    scrollBot()
                    this.update(app.$data.allData.allfriend, 'allfriend', id)
                }
                vjt.setStatusBarColor('in');
            },
            // 底部工具栏显示隐藏的切换
            hideFooter: function() {
                this.add_tools_key = false;
                this.face_tools_key = false;
            },
            clickFace: function() {
                var _this = this
                if (this.footer_bot != 0) {
                    setTimeout(function() {
                        _this.add_tools_key = false
                        _this.chat_input_key = true
                        _this.face_tools_key = true
                        scrollBot()
                    }, 200)
                } else {
                    this.add_tools_key = false
                    this.chat_input_key = true
                    this.face_tools_key = true
                    scrollBot()
                }
            },
            clickAdd: function() { //表情框隐藏，
                this.face_tools_key = false
                this.add_tools_key = !this.add_tools_key
                this.chat_input_key = true
                scrollBot()
            },
            chatInputFocus: function() {
                this.face_tools_key = false
                this.add_tools_key = false

                this.$nextTick(function() {
                    vjt.getKeyHeight()
                    scrollBot();
                })

            },
            chatInputChange: function() {
                if ($('#chat_input').html() != "") {
                    this.send_btn_key = true
                    // $("#chat_input").scrollTop($('#chat_input')[0].scrollHeight)
                } else {
                    this.send_btn_key = false
                }

            },
            switchYuyin: function() {
                this.chat_input_key = !this.chat_input_key
                this.face_tools_key = false
                this.add_tools_key = false
                if (!this.chat_input_key) { //如果是发语音，就要隐藏发送按钮
                    this.send_btn_key = false
                } else {
                    this.chatInputChange()
                }
            },
            // 点击表情
            disFace: function(e) {
                var el = $(e.currentTarget).clone(false);
                el.prop("contenteditable", "false")
                $(".chat_input").append(el)
                this.chatInputChange()
            },
            // 底部工具栏显示隐藏的切换
            // 选择头像
            mtPicChange: function(e, type) {},
            // 显示添加聊天页面
            addChat: function() {
                this.head_modal_key = false
                this.switchPage('add_chat', 1)
                this.is_newfriend = false
                this.add_chat_time = new Date()
                this.js_chat_time = new Date()
                    //这里随机图片和随机昵称
                $(".add_chat_icon").prop("src", vjt.getImg())
                $(".mt_new_chat_nic").val(vjt.getName())
                $(".add_chat_input input").val("")
            },
            //保存新聊天
            addChatSave: function() {
                var _this = this,
                    records = [],
                    input_key = false
                if (!$(".mt_new_chat_nic").val()) {
                    _this.$toast({
                        message: '请填写昵称',
                        position: 'bottom',
                        duration: 2000
                    });
                    return;
                }
                if (this.is_newfriend) { //打招呼内容
                    var msg01 = new Msg()
                    msg01.msg_type = 'system_time'
                    msg01.time = this.reTime(this.add_chat_time)
                    records.push(msg01)
                    $(".add_chat_input input").each(function(i, v) {
                        if ($(v).val()) {
                            input_key = true
                            var msg = new Msg()
                            msg.text = $(v).val()
                            msg.msg_type = 'text'
                            msg.isMe = false
                            records.push(msg)
                        }
                    })
                    if (!input_key) {
                        var msg = new Msg()
                        msg.text = "我是" + $(".mt_new_chat_nic").val()
                        msg.msg_type = 'text'
                        msg.isMe = false
                        records.push(msg)
                    }
                    var msg02 = new Msg()
                    msg02.msg_type = 'system'
                    msg02.text = '以上是打招呼的内容'
                    records.push(msg02)
                    var msg03 = new Msg()
                    msg03.msg_type = 'system_time'
                    msg03.time = this.reTime(this.js_chat_time)
                    records.push(msg03)
                    var msg04 = new Msg()
                    msg04.msg_type = 'system'
                    msg04.text = '你已添加了' + $(".mt_new_chat_nic").val() + '，现在可以开始聊天了。'
                    records.push(msg04)
                    var com_time = app.reTime02(_this.js_chat_time)
                } else {
                    var recent_new = ""
                    var com_time = app.reTime02(new Date())
                }

                id++;
                var new_chat = new Friend();
                new_chat.name = $(".mt_new_chat_nic").val()
                new_chat.icon = $(".add_chat_icon").prop("src")
                new_chat.com_time = com_time
                new_chat.id = id
                new_chat.is_chatlist = 1
                new_chat.chat_record = records
                back()
                this.acceptRequest(new_chat)
                this.updateSort() //聊天列表排序
                this.update(this.allData.allfriend, 'allfriend', id)
            },
            // 清空聊天列表
            emptyChat: function() {
                var _this = this
                this.head_modal_key = false
                this.$messagebox({
                    title: '提示',
                    message: '清空后聊天记录也将消失，确定清空聊天列表吗?',
                    showCancelButton: true
                })
                this.$messagebox.confirm('清空后聊天记录也将消失，确定清空聊天列表吗?').then(function(action) {
                    app.chatlist.forEach(function(obj) {
                        obj.is_chatlist = -1;
                        obj.chat_record = [];
                    })
                    _this.update(app.$data.allData.allfriend, 'allfriend', id)
                    _this.$toast({
                        message: '清空成功',
                        position: 'bottom',
                        duration: 2000
                    })
                })
            },
            // 判断是否是新好友
            addChatSwitch: function() {
                if ($(".add_chat .mint-switch").find("input").is(":checked")) {
                    this.is_newfriend = true;
                } else {
                    this.is_newfriend = false;
                }
            },
            wechatItemTouchStart: function(index, item, type, event) { //开始按压好友= =
                // if (!event._constructed) {
                //     return;
                // }
                this.touch_starttime = new Date().getTime()
                this.wechat_css = index
                var _this = this
                this.current_friend = item

                this.timer = setTimeout(function() {
                    if (type == 'wechat') {
                        _this.sheetVisible = true
                        _this.touch_index = index
                        _this.wechat_css = null
                    } else {
                        _this.mailsheetVisible = true
                    }
                }, 700)
            },
            wechatItemTouchMove: function(event) {
                this.wechat_css = null
                clearTimeout(this.timer)
                this.timer = null
            },
            switchChat: function(event) { //结束按压好友= =
                this.wechat_css = null
                var touch_endtime = new Date().getTime()
                if (!this.timer) //&& (this.timer !== 0)
                    return;
                if (touch_endtime - this.touch_starttime < 700) {
                    clearTimeout(this.timer)

                    this.switchPage('chat_window', 1)
                    this.current_friend.msg_num = 0

                    //每次进去聊天界面让聊天对象变为自己，延时是为了处理在系统版本6.0以下时会导致滚动条失效的bug。
                    setTimeout(function() {
                        app.$data.isMe = true;
                    }, 500)

                    //处理聊天窗口顶部的昵称过长,截取到两端文字，中间用省略号替代
                    var chat_window_header_text = $(".chat_window_header .transition_box_head_text")
                    if (chat_window_header_text.width() > 288) {
                        chat_window_header_text.text(getNameSubStr(chat_window_header_text.text()))
                    }

                    //初始化化底部工具栏
                    this.chatInputFocus();
                }
            },
            openPicker: function(add_chat, time) { //打开时间选择器
                this.cu_time_func = add_chat
                this.startDate = new Date(time)
                this.$refs.picker.open();
            },
            // setTime: function(time) { //按压好友设置时间
            //     var current_touch_i = this.touch_index
            //     this.chatlist[current_touch_i].com_time = this.reTime(time)
            //     this.updateSort()
            // },
            addChatTime: function(time) { //添加聊天时间
                this.add_chat_time = time
            },
            jsChatTime: function(time) { //接受聊天时间
                this.js_chat_time = time
            },
            handleConfirm: function(time) { //确认设置时间
                this.time_func[this.cu_time_func](this.reTime02(time))
                this.updateSort()
            },
            //更新聊天列表排序
            updateSort: function() {
                this.chatlist.sort(function(a, b) {
                    return new Date(b.com_time) - new Date(a.com_time)
                })
            },

            //**************************通讯录****************************** */
            showNewFriendPage: function() {
                this.switchPage('new_friend', 1)
                    // $(".mt_new_pic").prop("src", vjt.getImg())
            },
            switchAddFriendModal: function() {
                this.transition_box_head_modal_key = !this.transition_box_head_modal_key

                // vjt.addFriend()
            },
            hideAddFriendModal: function() {
                this.transition_box_head_modal_key = false;
            },
            disOneNewFriend: function() {
                this.switchPage('add_onefriend', 2)
                this.hideAddFriendModal();
                $(".mt_new_nic").val(vjt.getName())
                $(".mt_new_pic").prop("src", vjt.getImg())
            },
            //添加1个新朋友
            addNewFriend: function() {
                //获取姓名
                var name = $(".mt_new_nic").val()
                var pic_src = $(".mt_new_pic").prop("src")
                if (!name) {
                    this.$toast({
                        message: '请输入昵称',
                        position: 'bottom',
                        duration: 2000
                    });
                    return;
                }

                //获取验证信息
                var desc = $(".mt_yz_new_input").val()
                id++;
                //是否已添加
                var is_beenadd = this.is_beenadd
                var new_friend = new Friend()
                new_friend.name = name
                new_friend.icon = pic_src
                new_friend.id = id
                new_friend.request_state = is_beenadd
                new_friend.request_msg = desc
                this.allData.new_friend_list.unshift(new_friend)
                back();

                if (is_beenadd) { //如果直接接受请求，那通讯录也要更新
                    this.acceptRequest(new_friend)
                    this.update(this.allData.allfriend, 'allfriend', id)
                }
                this.update(this.allData.new_friend_list, 'new_friend_list', id)
            },
            //添加20条好友请求
            get20: function() {
                var data = JSON.parse(vjt.get20());
                data.forEach(function(obj) {
                    app.$data.allData.new_friend_list.unshift(obj)
                })
                id = id + 20;
                this.update(this.allData.new_friend_list, 'new_friend_list', id)
            },
            // 一次性接受全部好友请求
            acceptAll: function() {
                this.transition_box_head_modal_key = false;
                app.$messagebox({
                    title: '提示',
                    message: '亲爱的，确定要全部允许吗？',
                    showCancelButton: true
                })
                var arr = [];
                app.$messagebox.confirm('亲爱的，确定要全部允许吗？').then(function(action) {
                    $(app.$data.allData.new_friend_list).each(function(i, v) {
                        if (!v.request_state) {
                            v.request_state = true;
                            arr.push(v);
                        }
                    })
                    app.acceptRequest(arr);
                });
            },
            // 清空通讯录列表
            emptyNewFriendList: function() {
                this.transition_box_head_modal_key = !this.transition_box_head_modal_key
                var _this = this
                app.$messagebox({
                    title: '提示',
                    message: '亲爱的，确定要清空吗？',
                    showCancelButton: true
                })
                app.$messagebox.confirm('亲爱的，确定要清空吗？').then(function(action) {
                    app.$data.allData.new_friend_list = []
                    app.update(app.$data.allData.new_friend_list, 'new_friend_list', id)
                });
            },
            //更新通讯录排序
            // upMailListSort: function() {
            //     this.allData.allfriend.sort(function(a, b) {
            //         return b.type - a.type
            //     })
            // },
            //查找首字母
            rePinYin: function(item) {
                item.request_state = true
                var name = $.trim(item.name)
                var first_code = name.charAt(0) //取第一个字符

                //判断是汉字还是字母
                var reg = /^[\u4E00-\u9FA5]+$/
                var reg02 = /^[A-Za-z]+$/
                    // var reg03 = /^[0-9]+.?[0-9]*$/; //数字类型

                if (reg02.test(first_code)) { //如果是字母
                    return first_code.toUpperCase();

                } else if (reg.test(first_code)) {
                    return getInitials.convertPinyin(first_code);

                } else {
                    return '#';
                }
            },
            // 接受请求
            acceptRequest: function(item) {

                // 添加进通讯录
                if (Object.prototype.toString.call(item) == '[object Array]') {
                    //如果item是数组，那么就是一次性添加了多个好友，由全部接受按钮触发
                    $(item).each(function(i, v) {
                        czFriend(app.rePinYin(v), v)
                    })
                } else if (Object.prototype.toString.call(item) == '[object Object]') {
                    czFriend(this.rePinYin(item), item)
                } else {
                    return false;
                }

                this.$toast({
                    message: '已成功添加到通讯录',
                    position: 'bottom',
                    duration: 2000
                })

                this.sortMailList();
                this.update(this.allData.new_friend_list, 'new_friend_list', id)
                this.update(this.allData.allfriend, 'allfriend', id)

                function czFriend(code, item) {
                    var key = false
                    app.$data.allData.allfriend.forEach(function(obj) {
                        if (obj.type == code) {
                            obj.mail_list.push(item)
                            key = true
                        }
                    })
                    if (!key) {
                        app.$data.allData.allfriend.push({
                            type: code,
                            mail_list: [item]
                        })
                    }
                }
            },

            //********************个人信息********************** */
            //设置零钱
            recharge: function() {
                this.allData.my_info.small_change = parseInt($(".cz_pri_input").val())
                $(".cz_pri_input").val("")
                this.update(this.allData.my_info, 'my_info', id)
                back();
            },
            //设置昵称
            setNc: function() {
                var _this = this
                this.$messagebox({
                    title: '提示',
                    message: '设置昵称',
                    showCancelButton: true,
                    showInput: true,
                    inputType: 'text',
                    inputValue: _this.allData.my_info.my_nc
                })
                $(".mint-msgbox").css("top", '30%');
                this.$messagebox.prompt('设置昵称').then(function(obj) {
                    $(".mint-msgbox").css("top", '50%');
                    _this.allData.my_info.my_nc = obj.value
                    _this.update(_this.allData.my_info, 'my_info', id)
                }, function() {
                    $(".mint-msgbox").css("top", '50%');
                });
            },
            // 设置微信号
            setWxh: function() {
                var _this = this
                this.$messagebox({
                    title: '提示',
                    message: '设置微信号',
                    showCancelButton: true,
                    showInput: true,
                    inputType: 'text',
                    inputValue: _this.allData.my_info.my_wxh
                })
                $(".mint-msgbox").css("top", '30%');
                this.$messagebox.prompt('设置微信号').then(function(obj) {
                    $(".mint-msgbox").css("top", '50%');
                    _this.allData.my_info.my_wxh = obj.value
                    _this.update(_this.allData.my_info, 'my_info', id)
                }, function() {
                    $(".mint-msgbox").css("top", '50%');
                });
            },

            /**********************通用函数******************************** */
            //日期和数字补零
            zeroFill: function(num) {
                if (parseInt(num) < 10) {
                    return '0' + parseInt(num);
                } else {
                    return num;
                }
            },
            //简单处理用户输入的数据
            nameFilter: function(str) {
                if (typeof str === 'string') {
                    var s = "";
                    if (str.length == 0) return "";
                    s = str.replace(/&/g, "&gt;");
                    s = s.replace(/</g, "&lt;");
                    s = s.replace(/>/g, "&gt;");
                    s = s.replace(/ /g, "&nbsp;");
                    s = s.replace(/\'/g, "&#39;");
                    s = s.replace(/\"/g, "&quot;");
                    s = s.replace(/\n/g, "<br>");
                    return s;
                }
            },
            //聊天列表最近消息
            recentNew: function(item) {
                //用来循环聊天记录的一个递归，从最近一条聊天记录开始找，如果是时间消息就往前找，如果不是就输出这条记录，参数（聊天记录数组，索引）
                function xzMsg(arr, num) {
                    if (num === -1) {
                        return '';
                    }
                    if (arr[num].msg_type === 'system_time') {
                        xzMsg(arr, num - 1)
                    } else {
                        return arr[num]
                    }
                }
                var record = xzMsg(item.chat_record, item.chat_record.length - 1)
                if (!record) {
                    return;
                }
                var msg_type = record.msg_type;
                if (msg_type === 'text' || msg_type === 'system') {
                    return record.text;
                } else if (msg_type === 'audio') {
                    if (record.isRead || record.isMe) {
                        return '[语音]';
                    } else {
                        return '<span class="green_text">[语音]</span>';
                    }

                } else if (msg_type === 'video') {
                    return '[' + record.message + ']';

                } else if (msg_type === 'pic') {
                    return '[图片]';

                } else if (msg_type === 'msg_zz') {
                    if (record.isMe) {
                        if (!record.isReceive) {
                            return '[转账]待朋友收钱';
                        } else {
                            return '[转账]朋友已确认收钱';
                        }
                    } else {
                        if (!record.isReceive) {
                            return '[转账]请你确认收钱';
                        } else {
                            return '[转账]你已确认收钱';
                        }
                    }
                } else if (msg_type === 'msg_zz_ok') {
                    if (record.isMe) {
                        return '[转账]朋友已确认收钱';
                    } else {
                        return '[转账]你已确认收钱';
                    }
                } else if (msg_type === 'msg_hb') {

                    if (!record.isReceive) {
                        return '[微信红包]' + (record.message ? record.message : '恭喜发财，大吉大利');
                    }
                } else if (msg_type === 'receive_hb') {
                    if (record.isMe) {
                        return item.name + '领取了你的红包'
                    } else {
                        return '你领取了' + item.name + '的红包'
                    }
                }
            },
            //通讯录拼音排序
            sortMailList: function() {
                var arr = this.allData.allfriend
                var len = arr.length;
                for (var i = 0; i < len; i++) {
                    for (var j = 0; j < len - 1 - i; j++) {
                        if (arr[j].type > arr[j + 1].type) {
                            var temp = arr[j + 1];
                            arr[j + 1] = arr[j];
                            arr[j] = temp;
                        }
                    }
                }
                $(arr).each(function(index, obj) {
                    if (obj.type == "#") {
                        arr.splice(index, 1)
                        arr.push(obj)
                        return false;
                    }
                })
            },
            // 修改好友名称后通讯录重新排序
            sortMailFriendList: function(id) {
                var arr = this.allData.allfriend,
                    current_obj = {}
                $(arr).each(function(i, val) {
                    $(val.mail_list).each(function(index, obj) {
                        if (obj.id === id) {
                            if (app.rePinYin(obj) === val.type) { //如果拼音没变
                                return false;
                            } else {
                                //截取出这个好友,返回的是一个数组
                                current_obj = val.mail_list.splice(index, 1)

                                //如果截取后数组为空就删除整个数组
                                if (val.mail_list.length === 0) {
                                    arr.splice(i, 1)
                                }
                                var code = app.rePinYin(obj),
                                    key = false;
                                $(arr).each(function(index02, val02) {
                                    if (val02.type == code) {
                                        val02.mail_list.push(current_obj[0]);
                                        key = true;
                                    }
                                })
                                if (!key) {
                                    arr.push({
                                        type: code,
                                        mail_list: [current_obj[0]]
                                    })
                                    app.sortMailList();
                                }
                                return false;
                            }
                        }
                    })
                })

            },
            //调取安卓后台更新数据方法（更新的数据，更新数据类型，id）
            update: function(data, type, id) {
                data = JSON.stringify(data)
                vjt.update(data, type, id)
            },
            //获取随机昵称
            randomNc: function(el) {
                $(el).val(vjt.getName())
            },
            //返回xxxx-xx-xx xx:xx的时间格式
            reTime: function(now_date) {
                if (typeof now_date === 'string') {
                    now_date = new Date(now_date)
                }
                var now_time = now_date.getFullYear() + "-" + this.zeroFill(now_date.getMonth() + 1) +
                    '-' + this.zeroFill(now_date.getDate()) +
                    ' ' + this.zeroFill(now_date.getHours()) +
                    ":" + this.zeroFill(now_date.getMinutes())
                return now_time;
            },
            reTime02: function(now_date) {
                if (typeof now_date === 'string') {
                    now_date = new Date(now_date)
                }
                var now_time = this.reTime(now_date) + ":" + this.zeroFill(now_date.getSeconds())
                return now_time;
            },
            //根据当前时间返回日期(聊天列表)
            gshTime: function(time) {
                if (typeof time === 'string') {
                    time = new Date(time)
                }
                var com_year = time.getFullYear(),
                    com_month = time.getMonth() + 1,
                    com_date = time.getDate(),
                    com_hour = this.zeroFill(time.getHours()),
                    com_min = this.zeroFill(time.getMinutes()),
                    com_day = time.getDay();
                var current_date = new Date()
                var current_Year = current_date.getFullYear(),
                    current_month = current_date.getMonth() + 1,
                    current_date02 = current_date.getDate(),
                    current_day = current_date.getDay();

                if (com_year != current_Year) {
                    //如果设置年份小于当前年份,就只显示年月
                    return com_year + "年" + com_month + "月" + com_date + '日'

                } else {

                    if (com_month != current_month) { //如果设置月份不等于当前月份
                        return com_month + "月" + com_date + "日"

                    } else {
                        var day_disparity = current_date02 - com_date

                        if (day_disparity < 0) {
                            // day_disparity = 0
                            return com_month + "月" + com_date + '日'

                        } else if (day_disparity >= 2) {
                            if (this.isSameWeek(time, current_date) && com_day != 0) { //判断设置日期和当前日期是不是在同一周内
                                return this.weekday[com_day]

                            } else {
                                return com_month + "月" + com_date + '日'
                            }

                        } else if (day_disparity === 0) {
                            return com_hour + ":" + com_min

                        } else if (day_disparity === 1) {
                            return "昨天"
                        }
                    }
                }
            },
            //格式化聊天时间(聊天时间消息)
            gshChatTime: function(time) {
                if (typeof time === 'string') {
                    time = new Date(time)
                }
                var str = this.gshTime(time);

                var com_year = time.getFullYear(),
                    com_month = time.getMonth() + 1,
                    com_date = time.getDate(),
                    com_hour = this.zeroFill(time.getHours()),
                    com_min = this.zeroFill(time.getMinutes()),
                    com_day = time.getDay();
                var current_date = new Date();
                var current_Year = current_date.getFullYear(),
                    current_month = current_date.getMonth() + 1,
                    current_date02 = current_date.getDate();

                var day_disparity = current_date02 - com_date;

                if (com_year === current_Year && com_month === current_month && day_disparity === 0) { //如果是同年同月同日
                    return str;
                } else {

                    if (com_year != current_Year ||
                        com_month != current_month ||
                        (com_month == current_month && day_disparity < 0) ||
                        (com_month == current_month && day_disparity >= 2 && (!this.isSameWeek(time, current_date) || com_day == 0))) {
                        // 如果是不同年或者不同月或者不在同一周
                        // 就加上早上，中午，下午，晚上
                        var date_filter = "";
                        if (com_hour >= 0 && com_hour < 12) {
                            date_filter = '早上'
                        } else if (com_hour >= 12 && com_hour < 13) {
                            date_filter = '中午'
                        } else if (com_hour >= 13 && com_hour < 18) {
                            date_filter = '下午'
                        } else if (com_hour >= 18 && com_hour < 24) {
                            date_filter = '晚上'
                        }
                        str += ' ' + date_filter + com_hour + ":" + com_min;

                    } else {
                        str += ' ' + com_hour + ":" + com_min;
                    }
                    return str;
                }

            },
            //判断两个日期是否在同一周内
            isSameWeek: function(old, now) {
                var oneDayTime = 1000 * 60 * 60 * 24; //一天的毫秒数
                var old_count = parseInt(old.getTime() / oneDayTime); // 从1970年周四到这个时间一共有多少天
                var now_other = parseInt(now.getTime() / oneDayTime);
                //从1970年周四到这个时间一共有多少周
                return parseInt((old_count + 4) / 7) == parseInt((now_other + 4) / 7);
            },
            //切页面
            switchPage: function(el, type) {
                this.transition_el_type = type;
                if (type === 1) {
                    this.old_transition_el = this.transition_el;
                    this.transition_el = el;
                } else if (type === 2) {
                    this.old_transition_el02 = this.transition_el02;
                    this.transition_el02 = el;
                } else if (type === 3) {
                    this.old_transition_el03 = this.transition_el03;
                    this.transition_el03 = el;
                }
            },
            //初始化表情按钮的背景图片位置
            initFace: function() {
                var face_x = 0
                var face_y = 0
                $(".face_tools span").each(function(i, v) {
                    if (i % 12 === 0) {
                        face_x = 0
                        face_y = i / 12
                    } else {
                        face_x++
                    }
                    var x = -20.8 * face_x
                    var y = -20.8 * face_y
                        // var x = -30.9 * face_x
                        // var y = -30.9 * face_y
                    $(v).css("backgroundPosition", x + "px " + y + "px")
                })
            },
            // alertMsg: function(text, callback) {
            //     var _this = this
            //     this.tip_info_modal_key = true
            //     $(".tip_info_modal .tip_modal_text").text(text)
            //     $(".tip_info_qd").off()
            //     $(".tip_info_qd").on("click", function() {
            //         callback()
            //         _this.tip_info_modal_key = false
            //     })
            // },
            // alertInputMsg: function(text, callback) {
            //     var _this = this
            //     this.tip_input_modal_key = true
            //     $(".tip_input_modal .tip_modal_text").text(text)
            //         // $(".tip_modal_input").removeProp("autofocus")
            //     $(".tip_modal_input").prop("autofocus", "autofocus")
            //     $(".tip_input_qd").off()
            //     $(".tip_input_qd").on("click", function() {
            //         callback()
            //         _this.tip_input_modal_key = false
            //         $(".tip_input_modal .tip_modal_text").text("")
            //         $(".tip_modal_input").val("")
            //     })
            // }
        }
    })
}

$(function() {
    // window.onerror = handleErr
    // function handleErr(msg, url, l) {
    //     var txt = "";
    //     txt = "There was an error on this page.\n\n"
    //     txt += "Error: " + msg + "\n"
    //     txt += "URL: " + url + "\n"
    //     txt += "Line: " + l + "\n\n"
    //     txt += "Click OK to continue.\n\n"
    //     vjt.log(txt)
    //     return true
    // }

    //vue图片懒加载插件初始化
    Vue.use(VueLazyload, {
        preLoad: 1.3,
        error: '',
        loading: '',
        attempt: 3
    });

    initVue();

    if (typeof vjt === "object") {
        var data = JSON.parse(vjt.getData())

        app.$set(app.$data.allData, "new_friend_list", data.new_friend_list)
        app.$set(app.$data.allData, "allfriend", data.allfriend)
        app.$set(app.$data.allData, "my_info", data.my_info)
        id = vjt.getId()
    }
    // $(".footer_nav_item").click(function() {
    //     $(this).addClass("active").siblings().removeClass("active")
    //     $(".tab_content_item").hide().eq($(this).index()).show()
    // })
})