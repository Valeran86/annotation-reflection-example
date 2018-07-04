package ru.sbt.examples.homework;


public class testBeanUtils {
    public static void main( String[] args ) {
        FromClass fromClass = new FromClass( "Василий" , "Петров", 75, 170 ) ;
        ToClass toClass= new ToClass();
        BeanUtils.assign( toClass,fromClass );

        System.out.println("Ожидаем : Василий : " + toClass.getName()); //
        System.out.println("Ожидаем : Петров : " +toClass.getsName()); //
        System.out.println("Ожидаем : 0 : " +toClass.getAge()); //
        System.out.println("Ожидаем : 170 : " +toClass.getLength()); //

    }
    /**Класс для тестирования  ( откуда )
     * */
    public static class FromClass {
        private String _name ;
        private String _sName ;
        private int _age ;
        private Integer _length ; //  в классе To - это поле имеет тип  Number


        public FromClass (String name , String sName , int age, Integer length){
            this._name = name ;
            this._sName = name ;
            this._age = age ;
            this._length = length ;
        }

        public String getName() {
            return _name;
        }

        public String getsName() {
            return _sName;
        }

        public int getAge() {
            return _age;
        }

        public Integer getLength() {
            return _length;
        }
    }

    /**Класс для тестирования ( куда )
     * */
    public static class ToClass {
        private String name ;
        private String sName ;
        private byte age ;
        private Number length ;
        public ToClass () {};

        public String getName() {
            return name;
        }

        public void setName( String name ) {
            this.name = name;
        }

        public String getsName() {
            return sName;
        }

        public void setsName( String sName ) {
            this.sName = sName;
        }

        public byte  getAge() {
            return age;
        }

        public void setAge( byte age ) {
            this.age = age;
        }

        public Number getLength() {
            return length;
        }

        public void setLength( Number length ) {
            this.length = length;
        }
    }


}
