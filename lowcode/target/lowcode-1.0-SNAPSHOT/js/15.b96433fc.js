"use strict";(self["webpackChunklowcode"]=self["webpackChunklowcode"]||[]).push([[15],{8015:function(e,a,l){l.r(a),l.d(a,{default:function(){return g}});var t=l(3396),u=l(4870),n=l(7139),m=l(4161),s=l(3927),i=l(2074);const d=e=>((0,t.dD)("data-v-ca6c76d4"),e=e(),(0,t.Cn)(),e),o=d((()=>(0,t._)("span",null," 主集合名 ",-1))),c=d((()=>(0,t._)("span",null," 主集合属性 ",-1))),p=d((()=>(0,t._)("span",null," 补充集合名 ",-1))),r=d((()=>(0,t._)("span",null," 补充集合属性 ",-1)));var v={__name:"DataCollection",setup(e){const a=(0,u.iH)(50),l=(0,u.iH)(1),d=(0,u.iH)(10),v=(0,u.iH)(!1),h=(0,u.iH)([{key:1,mainSchema:[{segment:"name",type:"student",value:"hyh"}],subSchema:[[{segment:"name",type:"teacher",value:"XXX"}],[{segment:"name",type:"teacher",value:"xyz"}]]},{key:2,mainSchema:[{segment:"name",type:"student",value:"pzy"}],subSchema:[[{segment:"name",type:"teacher",value:"bb"}],[{segment:"name",type:"teacher",value:"aa"}]]},{key:3,mainSchema:[{segment:"name",type:"student",value:"hj"}],subSchema:[[{segment:"name",type:"teacher",value:"sss"}]]}]),f=(0,u.iH)([{schemaName:"student",fields:[{fieldName:"name"},{fieldName:"gender"},{fieldName:"number"},{fieldName:"project"}]},{schemaName:"teacher",fields:[{fieldName:"name"},{fieldName:"gender"},{fieldName:"number"},{fieldName:"age"},{fieldName:"project"}]},{schemaName:"project",fields:[{fieldName:"name"},{fieldName:"style"},{fieldName:"student"},{fieldName:"teacher"}]}]),g=(0,u.iH)(""),y=(0,u.iH)([]),w=(0,u.iH)(""),N=(0,u.iH)([]),k=(0,u.iH)(f.value.map((e=>({value:e.schemaName})))),S=(0,t.Fl)((()=>f.value.find((e=>e.schemaName==g.value))?.fields.map((e=>({value:e.fieldName}))))),b=(0,u.iH)(f.value.map((e=>({value:e.schemaName})))),x=(0,t.Fl)((()=>f.value.find((e=>e.schemaName==w.value))?.fields.map((e=>({value:e.fieldName}))))),_=[{title:"序号",dataIndex:"key",key:"key"},{title:"主集合",dataIndex:"mainSchema",key:"mainSchema"},{title:"补充集合",dataIndex:"subSchema",key:"subSchema"}];function H(){m.Z.get("dataShow.do",{params:{data:[{schema:g.value,fields:[...y.value]},{schema:w.value,fields:[...N.value]}]}}).then((e=>{h.value=e.data,v.value=!1})).catch((e=>{i.ZP.error("搜索失败"),console.log(e)}))}function W(e,a){}return(0,t.bv)((()=>{m.Z.get("getType.do").then((e=>{f.value=e.data.map((e=>(e.fields=e.fields.map((e=>({fieldName:e}))),{schemaName:e.typeName,fields:e.fields})))})).catch((e=>{console.log(e)}))})),(e,m)=>{const i=(0,t.up)("a-select"),f=(0,t.up)("a-col"),U=(0,t.up)("a-row"),z=(0,t.up)("a-button"),C=(0,t.up)("a-pagination"),j=(0,t.up)("a-divider"),I=(0,t.up)("a-table"),Z=(0,t.up)("a-layout");return(0,t.wg)(),(0,t.j4)(Z,{class:"container"},{default:(0,t.w5)((()=>[(0,t.Wm)(U,{style:{margin:"10px"},justify:"center"},{default:(0,t.w5)((()=>[(0,t.Wm)(f,{class:"gutter-row",span:6},{default:(0,t.w5)((()=>[o,(0,t.Wm)(i,{value:g.value,"onUpdate:value":m[0]||(m[0]=e=>g.value=e),options:k.value,style:{width:"200px"}},null,8,["value","options"])])),_:1}),(0,t.Wm)(f,{class:"gutter-row",span:6},{default:(0,t.w5)((()=>[c,(0,t.Wm)(i,{value:y.value,"onUpdate:value":m[1]||(m[1]=e=>y.value=e),"max-tag-count":3,options:S.value,mode:"multiple",style:{width:"200px"}},null,8,["value","options"])])),_:1}),(0,t.Wm)(f,{class:"gutter-row",span:6},{default:(0,t.w5)((()=>[p,(0,t.Wm)(i,{value:w.value,"onUpdate:value":m[2]||(m[2]=e=>w.value=e),options:b.value,style:{width:"200px"}},null,8,["value","options"])])),_:1}),(0,t.Wm)(f,{class:"gutter-row",span:6},{default:(0,t.w5)((()=>[r,(0,t.Wm)(i,{value:N.value,"onUpdate:value":m[3]||(m[3]=e=>N.value=e),"max-tag-count":3,options:x.value,mode:"multiple",style:{width:"200px"}},null,8,["value","options"])])),_:1})])),_:1}),(0,t.Wm)(z,{type:"primary",shape:"round",loading:v.value,class:"search-button",onClick:H},{icon:(0,t.w5)((()=>[(0,t.Wm)((0,u.SU)(s.Z))])),default:(0,t.w5)((()=>[(0,t.Uk)(" 搜索选定内容 ")])),_:1},8,["loading"]),(0,t.Wm)(C,{current:l.value,"onUpdate:current":m[4]||(m[4]=e=>l.value=e),pageSize:d.value,"onUpdate:pageSize":m[5]||(m[5]=e=>d.value=e),total:a.value,showSizeChanger:!1,"show-total":e=>`总共有 ${e} 条记录`,"show-less-items":"","show-quick-jumper":"",class:"pagination",onChange:m[6]||(m[6]=(e,a)=>W(e,a))},null,8,["current","pageSize","total","show-total"]),(0,t.Wm)(j,{style:{"margin-top":"0"}}),(0,t.Wm)(I,{dataSource:h.value,columns:_,pagination:!1,loading:v.value,size:"small",style:{"margin-left":"10px","margin-right":"10px"},bordered:""},{bodyCell:(0,t.w5)((({column:e,text:a,record:l})=>["mainSchema"===e.dataIndex?((0,t.wg)(),(0,t.iD)(t.HY,{key:0},[(0,t.Uk)((0,n.zw)(a),1)],64)):"subSchema"===e.dataIndex?((0,t.wg)(),(0,t.iD)(t.HY,{key:1},[(0,t.Uk)((0,n.zw)(a),1)],64)):(0,t.kq)("",!0)])),_:1},8,["dataSource","loading"])])),_:1})}}},h=l(89);const f=(0,h.Z)(v,[["__scopeId","data-v-ca6c76d4"]]);var g=f}}]);
//# sourceMappingURL=15.b96433fc.js.map