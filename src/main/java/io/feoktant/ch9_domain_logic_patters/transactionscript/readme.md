Changes to the original code:
- [`MfDate`](https://martinfowler.com/eaaDev/TimePoint.html), changed to `LocalDate`
- Custom [`Money`](https://martinfowler.com/eaaCatalog/money.html), changed to [Moneta](https://javamoney.github.io/ri.html)
- Added one contract and product for example in `Main`
- Inlined SQL strings into methods
- `findContractStatement` missed `c.ID`
