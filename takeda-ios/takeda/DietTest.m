//
//  DietTest.m
//  takeda
//
//  Created by Alexander Rudenko on 26.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "DietTest.h"

@interface DietTest (){
    NSMutableArray *test_data;
}

@end

@implementation DietTest

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
    [self.tableView reloadData];
}

-(void)setupInterface{
    [self.getRecommForDiet setupStandartBordered];
    self.getRecommForDiet.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:15.0];

    self.tLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:17.0f];
    self.tableView.backgroundColor = RGB(243, 243, 243);
    
    [self.tableView.tableHeaderView addSeparatorColor:RGB(186, 189, 191)];
    [self.tableView.tableFooterView addTopSeparatorColor:RGB(186, 189, 191)];

}

-(void)initData{
    test_data = [Global recursiveMutable: @[@{@"key":@"cakes", @"title":@"Торты/мороженое",@"state":@"2"},
                                            @{@"key":@"milk", @"title":@"Жирный сыр/сливки/желток яиц/цельное молоко/йогурты",@"state":@"2"},
                                            @{@"key":@"fat", @"title":@"Сливочное масло/маргарин/пальмовое кокосовое масло/сало/соусы с яичным желтком (майонез)",@"state":@"2"},
                                            @{@"key":@"cocos", @"title":@"Кокос",@"state":@"2"},
                                            @{@"key":@"fritur", @"title":@"Любая пища, приготовления во фритюре",@"state":@"2"}
                                            ]];
}

#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return test_data.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSMutableDictionary *item = test_data[indexPath.row];
    float tH = [Global text:item[@"title"] sizeWithFont:[self cellTitleFont] constrainedToSize:CGSizeMake(tableView.width - 30, CGFLOAT_MAX)].height;
    return tH + 80;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"DietTestTableCell";
    
    DietTestTableCell *cell = (DietTestTableCell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"DietTestTableCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[DietTestTableCell class]])
            {
                cell = (DietTestTableCell *)currentObject;
                break;
            }
        }
    }
    
    NSMutableDictionary *item = test_data[indexPath.row];
    cell.title.text = item[@"title"];
    cell.title.font = [self cellTitleFont];
    cell.segment.selectedSegmentIndex = [item[@"state"] intValue];
    cell.title.height = [Global text:item[@"title"] sizeWithFont:[self cellTitleFont] constrainedToSize:CGSizeMake(tableView.width - 30, CGFLOAT_MAX)].height;
    cell.segment.y = cell.title.bottom+10;
    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

#pragma mark -

-(void)selectIndex:(UISegmentedControl*)sControl{
    
}

-(IBAction)getRecommendationAction:(id)sender{
    
}

-(UIFont*)cellTitleFont{
    return [UIFont fontWithName:@"Helvetica-Light" size:14];
}

@end
