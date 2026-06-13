FlukeEvents.explosionRecipes(event => {

  event.add('test_1', [
    { item: 'minecraft:iron_nugget', count: 3, chance: 1.0 },
    { item: 'minecraft:iron_ingot', count: 1, chance: 0.25 }
  ])

  event.add({
    id: 'test_2',
    input: 'minecraft:coal',
    outputs: [
      { item: 'minecraft:diamond', count: 1, chance: 0.05 },
      { item: 'minecraft:charcoal', count: 1, chance: 0.5 }
    ]
  })
})
