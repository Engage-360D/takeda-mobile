//
//  Daybooks.m
//  takeda
//
//  Created by Alexander Rudenko on 22.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "Daybooks.h"

@interface Daybooks (){
    NSMutableArray *menu_data;
}

@end

@implementation Daybooks

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
}

-(void)initData{
//    menu_data = [Global recursiveMutable:@[@{@"date":@"1991-01-19T00:00:00+0000",@"list":@[
//                                                                    @{@"title":@"Физическая нагрузка",@"rightTitle":@"мин", @"type":[NSNumber numberWithInt:ctLeftCaptionRightCaption],@"action":[NSNumber numberWithInt:physExercises]},
//                                                                    @{@"title":@"Физическая нагрузка",@"rightTitle":@"мин", @"type":[NSNumber numberWithInt:ctLeftCaptionRightCaption],@"action":[NSNumber numberWithInt:physExercises]},
//                                                                    @{@"title":@"Физическая нагрузка",@"rightTitle":@"мин", @"type":[NSNumber numberWithInt:ctLeftCaptionRightCaption],@"action":[NSNumber numberWithInt:physExercises]},
//                                                                    @{@"title":@"Физическая нагрузка",@"rightTitle":@"мин", @"type":[NSNumber numberWithInt:ctLeftCaptionRightCaption],@"action":[NSNumber numberWithInt:physExercises]},
//                                                                    
//                                                                    ] },
//                                           
//                                           
//                                           
//                                           
//                                           
//                                           ]];
}

-(IBAction)selectViewType:(UISegmentedControl*)sender{
    [self selectView:sender.selectedSegmentIndex];
}

-(void)selectView:(int)index{
    switch (index) {
        case 0:{

            
            break;
        }
        case 1:{

            
            break;
        }
    }
    
    [self.tableView reloadData];

}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return menu_data.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [menu_data[section] count];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section == 0){
        return 0;
    } else {
        return 75.f;
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"StandartCombyCell";
    
    StandartCombyCell *cell = (StandartCombyCell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"StandartCombyCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[StandartCombyCell class]])
            {
                cell = (StandartCombyCell *)currentObject;
                break;
            }
        }
    }
    
    NSMutableDictionary *menu = menu_data[indexPath.section][indexPath.row];
    cell.cellType = [menu[@"type"] intValue];
    cell.caption.text = menu[@"title"];
    cell.subTitle.text = menu[@"subtitle"];
    cell.rightCaption.text = menu[@"rightTitle"];
    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//    NSMutableDictionary *item = menu_data[indexPath.section][indexPath.row];
//    switch ([item[@"action"] intValue]) {
//            
//            
//    }
    
    
    [self.tableView reloadData];
}




@end
