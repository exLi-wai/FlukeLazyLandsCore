FlukeEvents.explosionRecipes(event => {

  event.add('minecraft:raw_iron', [
    { item: 'minecraft:iron_nugget', count: 3, chance: 1.0 },
    { item: 'minecraft:iron_ingot', count: 1, chance: 0.25 }
  ])

  event.add({
    id: 'test_1',
    input: 'minecraft:coal',
    outputs: [
      { item: 'minecraft:diamond', count: 1, chance: 0.05 },
      { item: 'minecraft:charcoal', count: 1, chance: 0.5 }
    ]
  })

  event.add({
    id: 'test_2',
    input: '3 minecraft:coal_block',
    outputs: [
      { item: 'minecraft:diamond', count: 1, chance: 0.25 }
    ]
  })

})

