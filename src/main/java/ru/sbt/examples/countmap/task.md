#Task3
##Реализовать следующий класс по документации
Scans object "from" for all getters. If object "to"
- contains correspondent setter, it will invoke it to set property value for "to" which equals to the property of "from".
- The type in setter should be compatible to the value returned by getter (if not, no invocation performed).
- Compatible means that parameter type in setter should be the same or be superclass of the return type of the getter.
-  The method takes care only about public methods.

@param to   Object which properties will be set.

@param from Object which properties will be used to get values.



##CountManImpl
Создать экземпляр класса CountMapImpl, проинициализировать его и затем попытаться достать, каким типом инициализирован параметр класса из внешнего класса и в методе, внутри объекта.
